from flask import Flask, jsonify, render_template, request
from flask_cors import CORS
import requests
from bs4 import BeautifulSoup

''' Raspberry pi Sensor Check '''
sensor_check = 0
try :
    import time
    import board
    import adafruit_dht
    sensor_check = 1
except :
    pass

app = Flask(__name__)
CORS(app)  # CORS를 전체 앱에 적용

@app.route('/api/finance')
def get_kospi():
    response = requests.get('https://finance.naver.com/')
    html = response.text
    soup = BeautifulSoup(html, 'html.parser')
    kospi = soup.select_one('#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kospi_area.group_quot').text
    kospi = list(kospi.split("\n"))
    kospi_value = kospi[5]
    kospi_change_value = kospi[6]
    kospi_change_rate = kospi[7]
    if kospi[8] == "상승" :
        kospi_type = "▲"
    else :
        kospi_type = "▼"
    kosdaq = soup.select_one('#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kosdaq_area.group_quot').text
    kosdaq = list(kosdaq.split("\n"))
    kosdaq_value = kosdaq[5]
    kosdaq_change_value = kosdaq[6]
    kosdaq_change_rate = kosdaq[7]
    if kosdaq[8] == "상승" :
        kosdaq_type = "▲"
    else :
        kosdaq_type = "▼"
    
    rate = soup.select_one("#content > div.article2 > div.section1 > div.group1 > table > tbody > tr.up.bold > td:nth-child(2)").text
    rate_change = soup.select_one("#content > div.article2 > div.section1 > div.group1 > table > tbody > tr.up.bold > td:nth-child(3)").text
    rate_change_value = rate_change.split(" ")[-1]
    if "상승" in rate_change :
        rate_type = "▲"
    else :
        rate_type = "▼"

    kospi_message = "{} {}  {}({})".format(kospi_type, kospi_value, kospi_change_value, kospi_change_rate)
    kosdaq_message = "{} {}  {}({})".format(kosdaq_type, kosdaq_value, kosdaq_change_value, kosdaq_change_rate)
    rate_message = "{} {}  ({})".format(rate_type, rate, rate_change_value)
    return jsonify(kospi = kospi_message, kosdaq = kosdaq_message, rate = rate_message)

@app.route('/api/news')
def get_news() :
    url = "https://news.sbs.co.kr/news/newsflash.do"
    response = requests.get(url)
    html = response.text
    soup = BeautifulSoup(html, 'html.parser')
    news_datas = []
    for i in range(1, 11) :
        try :
            news_text = soup.select_one('#container > div > div.w_news_list.type_issue2 > ul > li:nth-child(' + str(i) + ') > a > p > strong').text
            news_detail = soup.select_one('#container > div > div.w_news_list.type_issue2 > ul > li:nth-child(' + str(i) + ') > a > p > span.read').text
            news_datas.append({news_text.replace("'", '"').strip() : news_detail})
        except :
            break
    return jsonify(news_datas = news_datas)
    
@app.route("/api/location")
def location() :
    latitude = request.args.get('lat', type=float)
    longitude = request.args.get('lng', type=float)
    return render_template("traffic.html", lat = latitude, lng = longitude)

@app.route("/api/dht")
def get_dht() :
    if sensor_check == 1 :
        sensor = adafruit_dht.DHT11(board.D2)
        temp = sensor.temperature
        humi = sensor.humidity
        sensor.exit()
        return jsonify(result = "success", temp = "{}".format(temp), humi = "{}".format(humi))
    else :
        return jsonify(result = "fail", message = "Please Connect Sensor Module")
    

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5555, debug=True)