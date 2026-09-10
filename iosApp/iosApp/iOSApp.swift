import SwiftUI
import Shared

import GoogleSignIn

@main
struct iOSApp: App {

    init() {
        // Hands the Swift implementation to the shared module. Until this runs, the shared
        // client reports itself unavailable and the Google button stays inert.
        GoogleSignInBridgeRegistry.shared.bridge = GoogleSignInBridgeImpl()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                // Google returns through a URL callback; without this the sheet never completes.
                .onOpenURL { url in GIDSignIn.sharedInstance.handle(url) }
        }
    }
}
