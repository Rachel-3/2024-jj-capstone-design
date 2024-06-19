import React, { useState, useEffect } from 'react';
import './App.css';
import CameraFeed from './components/CameraFeed';
import DateTime from './components/DateTime';
import Weather from './components/Weather';
import CheerUp from './components/CheerUp';
import Login from './components/Login';
import Finance from './components/Finance';
import News from './components/News';
import Message from './components/Message';
import Traffic from './components/Traffic';
import DHT from './components/DHT';
import Start from './components/Start';
import Schedule from './components/Schedule';
import axios from 'axios';
function App() {
    const [widgets, setWidgets] = useState([]);
    const [showText, setShowText] = useState(false);
    const [userName, setUserName] = useState("Guest");
    const [userid, setUserId] = useState("Guest");
    const [isActive, setIsActive] = useState(false);
    const [showStart, setShowStart] = useState(false);
    const [lastUserId, setLastUserId] = useState("Guest");
    const [userLostTimeout, setUserLostTimeout] = useState(null);
    const [localDate, setLocalDate] = useState('');

    useEffect(() => {
        const fetchWidgets = async () => {
            try {
                const response = await axios.get('https://jj.system32.kr/widgets_index');
                const messageOnly = response.data.message;
                const mappedWidgets = Object.entries(messageOnly).map(([type, position]) => ({
                    type,
                    row: position[0],
                    col: position[1]
                }));
                setWidgets(mappedWidgets);
            } catch (error) {
                console.error('Error fetching widgets:', error);
            }
        };

        const getCurrentDate = () => {
            const today = new Date();
            console.log("Current Date in App.js:", today.toISOString().split('T')[0]);
            setLocalDate(today.toISOString().split('T')[0]);
        };

        fetchWidgets();
        getCurrentDate();

        const timer = setTimeout(() => setShowText(true), 1000);
        return () => clearTimeout(timer);
    }, []);
    const handleUserDetection = async (active, name = "Guest", id = "Guest") => {
        if (active) {
            if (id !== lastUserId) {
                setUserName(name);
                setUserId(id);
                setLastUserId(id);
                setShowStart(true);
                try {
                    const loginResponse = await axios.post('https://jj.system32.kr/get_widgets_custom/username=' + id);
                    if (loginResponse.data.message === "Empty Database") {
                        try {
                            const response = await axios.get('https://jj.system32.kr/widgets_index');
                            const messageOnly = response.data.message;
                            const mappedWidgets = Object.entries(messageOnly).map(([type, position]) => ({
                                type,
                                row: position[0],
                                col: position[1]
                            }));
                            setWidgets(mappedWidgets);
                        } catch (error) {
                            console.error('Error fetching widgets:', error);
                        }
                    } else {
                        const mappedWidgets = Object.entries(loginResponse.data.message).map(([type, position]) => ({
                            type,
                            row: position[0],
                            col: position[1]
                        }));
                        setWidgets(mappedWidgets);
                    }
                } catch (error) {
                    console.error('Error logging in:', error);
                } finally {
                    setTimeout(() => setShowStart(false), 1000);
                }
            } else {
                setShowStart(false);
            }
            setIsActive(true);
            if (userLostTimeout) {
                clearTimeout(userLostTimeout);
                setUserLostTimeout(null);
            }
        } else {
            if (!userLostTimeout) {
                const timeout = setTimeout(() => {
                    setIsActive(false);
                    setUserName("Guest");
                    setUserId("Guest");
                    setWidgets([]);
                    setUserLostTimeout(null);
                }, 5000);
                setUserLostTimeout(timeout);
            }
        }
    };

    return (
        <div className={`container ${showText ? 'show' : ''}`}>
            {showStart ? (
                <Start userName={userName} />
            ) : (
                <>
                    {!isActive ? (
                        <div className="black-screen"></div>
                    ) : (
                        [1, 2, 3].map(row => (
                            <div className="row" key={row} style={{ height: "330px" }}>
                                {[1, 2, 3, 4].map(col => (
                                    <div className="col" key={col}>
                                        {widgets.find(widget => widget.row === row && widget.col === col) ? (
                                            (() => {
                                                const widget = widgets.find(widget => widget.row === row && widget.col === col);
                                                switch (widget.type) {
                                                    case 'DateTime': return <DateTime key={`${row}-${col}`} />;
                                                    case 'Weather': return <Weather key={`${row}-${col}`} />;
                                                    case 'CheerUp': return <CheerUp key={`${row}-${col}`} userName={userName} />;
                                                    case 'Login': return <Login key={`${row}-${col}`} />;
                                                    case 'Finance': return <Finance key={`${row}-${col}`} />;
                                                    case 'News': return <News key={`${row}-${col}`} />;
                                                    case 'Traffic': return <Traffic key={`${row}-${col}`} />;
                                                    case 'Message': return <Message key={`${row}-${col}`} userName={userName} />;
                                                    case 'Room': return <DHT key={`${row}-${col}`} />;
                                                    case 'Todo': return <Schedule key={`${row}-${col}`} username={userName} localdate={localDate} />;
                                                    default: return null;
                                                }
                                            })()
                                        ) : null}
                                    </div>
                                ))}
                            </div>
                        ))
                    )}
                </>
            )}
            <CameraFeed onUserDetected={handleUserDetection} />
        </div>
    );
}
export default App;