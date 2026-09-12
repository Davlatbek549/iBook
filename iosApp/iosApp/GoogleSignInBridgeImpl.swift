import Foundation
import Shared
import UIKit

import GoogleSignIn

/// Reaches GoogleSignIn-iOS on behalf of the shared module.
///
/// Kotlin cannot call Swift, so `GoogleSignInBridge` is declared in `iosMain` and satisfied here.
/// Everything past the returned token — trading it for a session, storing it, refreshing it —
/// is shared code and identical to Android.
///
/// The token is *not* trusted here. It goes straight to `dz-server`, which verifies the signature
/// against Google's published keys before believing any of it.
final class GoogleSignInBridgeImpl: NSObject, GoogleSignInBridge {

    /// The **iOS** OAuth client id: it identifies this app to Google, and its reversed form is the
    /// URL scheme in Info.plist that the sign-in sheet returns through.
    ///
    /// It is not what the token is issued for. dz-server accepts exactly one audience, the **Web**
    /// client, so the token has to be minted for that — see `signIn`.
    private static let clientId =
        "169301208092-sj71d0poosh9h0sj1mp79h496h97g49l.apps.googleusercontent.com"

    var isSupported: Bool { true }

    func signIn(onResult: @escaping (String?, String?) -> Void) {
        guard let presenter = Self.topViewController() else {
            onResult(nil, "No screen available to present sign-in")
            return
        }

        // Naming the Web client as the server makes GoogleSignIn send it to Google as the token's
        // `audience`, so the ID token comes back issued for the Web client — the same thing
        // Android's setServerClientId does. Without it the token was issued for the iOS client,
        // and dz-server, which checks for the Web client alone, refused every one of them.
        //
        // The id comes from the shared module, so iOS and Android cannot drift apart on it.
        GIDSignIn.sharedInstance.configuration = GIDConfiguration(
            clientID: Self.clientId,
            serverClientID: GoogleAuthConfig.shared.SERVER_CLIENT_ID
        )
        GIDSignIn.sharedInstance.signIn(withPresenting: presenter) { result, error in
            if let error = error as NSError? {
                // Dismissing the sheet is a choice, not a failure: both nil means cancelled.
                if error.code == GIDSignInError.canceled.rawValue {
                    onResult(nil, nil)
                } else {
                    onResult(nil, error.localizedDescription)
                }
                return
            }
            guard let idToken = result?.user.idToken?.tokenString else {
                onResult(nil, "Google returned no ID token")
                return
            }
            onResult(idToken, nil)
        }
    }

    /// The presented sheet needs a live view controller; Compose hosts everything under one root.
    private static func topViewController() -> UIViewController? {
        let scene = UIApplication.shared.connectedScenes
            .compactMap { $0 as? UIWindowScene }
            .first { $0.activationState == .foregroundActive }

        var controller = scene?.keyWindow?.rootViewController
        while let presented = controller?.presentedViewController {
            controller = presented
        }
        return controller
    }
}
