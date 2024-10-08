import SwiftUI

struct UserProductView: View {
    @State private var users: [String] = []
    @State private var isLoading = false
    @State private var Username = UserDefaults.standard.string(forKey: "Username") ?? "Guest"
    @State private var Password = UserDefaults.standard.string(forKey: "Password") ?? "Guest"
    @State private var modelCode: String = UserDefaults.standard.string(forKey: "model_code") ?? "0000-0000"
    @State private var showAlert = false
    @State private var alertMessage = ""
    @Environment(\.presentationMode) var presentationMode
    
    var body: some View {
        VStack {
            Spacer()
            Image("Logo")
                .resizable()
                .frame(width: 120, height: 170)
            Text("BLUR")
                .foregroundColor(Color.mainText)
                .frame(width: 700)
                .font(.system(size: 62))
                .fontWeight(.bold)
            Text("Beyond Limitations, Unleashing Reflection")
                .foregroundColor(Color.mainsubText)
                .font(.system(size: 14))
            Spacer()

            List(users, id: \.self) { user in
                VStack(alignment: .leading) {
                    Text(user)
                        .font(.headline)
                }
            }
            .onAppear {
                if modelCode != "0000-0000" {
                    loadUserData()
                } else {
                    alertMessage = "Register Production First"
                    showAlert = true
                }
            }
            .toolbar {
                EditButton()
            }
            Spacer()
            Button(action: {
                remove_production()
            }, label: {
                Text("등록 해제")
            })
        }
        .alert(isPresented: $showAlert) {
            Alert(
                title: Text("Message"),
                message: Text(alertMessage),
                dismissButton: .default(Text("OK")) {
                    // Navigate back to the previous view
                    self.presentationMode.wrappedValue.dismiss()
                }
            )
        }
    }
    
    func remove_production() {
        isLoading = true
        Task {
            do {
                let apiModel = try await ProductionService.shared.removeProduction(username: Username, password: Password, model_code: modelCode)
                isLoading = false // 작업이 완료되면 로딩 상태를 false로 설정
                if !isLoading {
                    alertMessage = apiModel.result ?? "Error"
                    showAlert = true
                    UserDefaults.standard.removeObject(forKey: "model_code")
                }
            } catch {
                print("Error:", error)
            }
            isLoading = false
        }
    }
    
    func loadUserData() {
        isLoading = true
        Task {
            do {
                let response = try await ProductionService.shared.getProductionUsers(model_code: modelCode)
                if let message = response.message {
                    decodeMessageAndSetUsers(message)
                }
            } catch {
                print("Error:", error)
            }
            isLoading = false
        }
    }
    
    func decodeMessageAndSetUsers(_ message: String) {
        // JSON 형식으로 문자열을 수정합니다.
        let jsonString = message.replacingOccurrences(of: "'", with: "\"")
        if let jsonData = jsonString.data(using: .utf8) {
            do {
                let decodedUsers = try JSONDecoder().decode([String].self, from: jsonData)
                self.users = decodedUsers
            } catch {
                print("Error decoding message: \(error)")
            }
        }
    }
}
