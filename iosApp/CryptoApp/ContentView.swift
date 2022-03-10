import SwiftUI
import domain

struct ContentView: View {
    var body: some View {
        Text(DomainCrypto(
            id: "", name: "", imageUrl: "", price: 22, symbol: "Crypto", priceChangePercentIn24h: 22.0, order: 3).symbol)
            .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
