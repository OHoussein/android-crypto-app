import Combine
import domain

//public struct CryptoListPublisher: Publisher {
//
//
//    public typealias Output = DomainCrypto
//
//    public typealias Failure = Never
//
//
//    private let repository: ICryptoRepository
//
//    public init(repository: ICryptoRepository) {
//        self.repository = repository
//    }
//
//    public func receive<S>(subscriber: S) where S : Subscriber, Never == S.Failure, DomainCrypto == S.Input {
//        <#code#>
//    }
//
//    final class CryptoListSubscription<S: Subscriber>: Subscription where S.Input == DomainCrypto, S.Failure == Failure {
//        func request(_ demand: Subscribers.Demand) {
//            <#code#>
//        }
//
//        func cancel() {
//            <#code#>
//        }
//    }
//
//}
