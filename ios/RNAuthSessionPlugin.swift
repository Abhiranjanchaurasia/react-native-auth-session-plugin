import Foundation
import AuthenticationServices
import LocalAuthentication
import React

@objc(RNAuthSessionPlugin)
class RNAuthSessionPlugin: NSObject {
    private var session: ASWebAuthenticationSession?

    @objc(login:callbackScheme:resolver:rejecter:)
    func login(loginUrl: NSString, callbackScheme: NSString, resolver: @escaping RCTPromiseResolveBlock, rejecter: @escaping RCTPromiseRejectBlock) {
        authenticateAndStartSession(urlString: loginUrl as String, callbackScheme: callbackScheme as String, resolver: resolver, rejecter: rejecter)
    }

    @objc(logout:callbackScheme:resolver:rejecter:)
    func logout(logoutUrl: NSString, callbackScheme: NSString, resolver: @escaping RCTPromiseResolveBlock, rejecter: @escaping RCTPromiseRejectBlock) {
        authenticateAndStartSession(urlString: logoutUrl as String, callbackScheme: callbackScheme as String, resolver: resolver, rejecter: rejecter)
    }

    private func authenticateAndStartSession(urlString: String, callbackScheme: String, resolver: @escaping RCTPromiseResolveBlock, rejecter: @escaping RCTPromiseRejectBlock) {
        let context = LAContext()
        var error: NSError?
        let reason = "Authenticate to continue"

        if context.canEvaluatePolicy(.deviceOwnerAuthentication, error: &error) {
            context.evaluatePolicy(.deviceOwnerAuthentication, localizedReason: reason) { success, authError in
                DispatchQueue.main.async {
                    if success {
                        self.startWebAuthSession(urlString: urlString, callbackScheme: callbackScheme, resolver: resolver, rejecter: rejecter)
                    } else {
                        rejecter("biometric_failed", "Authentication failed", authError)
                    }
                }
            }
        } else {
            rejecter("biometric_unavailable", "Authentication not available", error)
        }
    }

    private func startWebAuthSession(urlString: String, callbackScheme: String, resolver: @escaping RCTPromiseResolveBlock, rejecter: @escaping RCTPromiseRejectBlock) {
        guard let url = URL(string: urlString) else {
            rejecter("invalid_url", "Invalid URL provided", nil)
            return
        }

        session = ASWebAuthenticationSession(url: url, callbackURLScheme: callbackScheme) { callbackURL, error in
            if let callbackURL = callbackURL {
                resolver(callbackURL.absoluteString)
            } else if let error = error {
                rejecter("auth_session_error", error.localizedDescription, error)
            } else {
                rejecter("unknown_error", "Authentication session failed without error", nil)
            }
        }

        if #available(iOS 13.0, *) {
            session?.presentationContextProvider = self
        }

        session?.start()
    }
}

@available(iOS 13.0, *)
extension RNAuthSessionPlugin: ASWebAuthenticationPresentationContextProviding {
    func presentationAnchor(for session: ASWebAuthenticationSession) -> ASPresentationAnchor {
        if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene {
            return windowScene.windows.first(where: { $0.isKeyWindow }) ?? ASPresentationAnchor()
        }
        return ASPresentationAnchor()
    }
}
