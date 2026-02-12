import SwiftUI
import GeoBlinker

/**
 * Main iOS app view
 * Wraps the Compose Multiplatform UI
 */
struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea(.all) // Full screen compose
    }
}

/**
 * Compose UI wrapper for SwiftUI
 * Bridges between SwiftUI and Compose Multiplatform
 */
struct ComposeView: UIViewControllerRepresentable {
    
    func makeUIViewController(context: Context) -> UIViewController {
        // Create and return the main Compose view controller
        MainViewControllerKt.MainViewController()
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // Handle updates if needed
    }
}

#if DEBUG
struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
#endif
