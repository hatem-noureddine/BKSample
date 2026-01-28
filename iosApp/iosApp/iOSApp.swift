import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        KoinFactory.shared.doInitKoin(config: nil)
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}