# WEB PUSH

## Overview

[Push Api](https://developer.mozilla.org/ko/docs/Web/API/Push_API)를 이용한 푸시 기능을 지원한다. Push Api는 지금도 활발한 논의와 변경이 일어나고 있다. [Draft](https://www.w3.org/TR/push-api/)    
웹소켓과 같은 기술로도 알림을 구현할 수 있다. 그러나 웹소켓을 이용한 방법은 offline 상황에서 알림을 보낼 수 없다.    
Push Api는 [Service Worker](https://developer.mozilla.org/ko/docs/Web/API/Service_Worker_API)를 이용하므로 offline에서도 알림을 송수신할 수 있다.    

Push Api는 브라우저별로 구현이 다르다. 크롬은 FCM, Firebase Cloud Messaging, 파이어폭스는 Auto Push로 Push Api를 구현하고 있다. Safari는 ServiceWorker를 지원하지 않으므로 **Web Push를 사용할 수 없다**.

동작 흐름은 다음과 같다.
### Subscription
![subscription](https://user-images.githubusercontent.com/40727649/90748917-c193f280-e30d-11ea-94c4-0471544108d1.png)

### Push
![push](https://user-images.githubusercontent.com/40727649/90748923-c35db600-e30d-11ea-8cc7-3076a5a17fac.png)

## Terms

- End User  
    알림을 받을 최종 사용자.
    
- Application Server  
    End User에게 알림을 보내고자 하는 어플리케이션.
  
- Push Service  
    Application Server로부터 알림을 요청받아 End User에게 알림을 전송한다. c.f) FCM, GCM, APN ...

- [VapidKey](https://tools.ietf.org/html/draft-ietf-webpush-vapid-01)  
    Application Server가 스스로를 인증하기 위해 발행하는 키 쌍이다. End User는 Application Server로부터 VapidKey를 받아 Push Service에 전달한다. Push Service는 VapidKey를 검증하고 End User에게 `Subscription`을 발행한다.  
    Application Server가 Push Service에게 알림을 요청할 때, 스스로를 인증하기 위해 VapidKey를 이용한 encrypt를 수행한다.

- `Subscription`  
    Application Server가 End User로 알림을 보내기 위해 필요한 정보.  
    Application Server가 End User에게 알림을 보내고자 할 때, `Subscription`을 이용해서 Push Service에 요청하게 된다.  
    Push Service가 `Subscription`을 생성할 때, `VapidKey`가 필요하다. 해당 `Subscription`은 `VapidKey`를 발행한 Application Server만이 사용할 수 있다.  
    End User는 A의 알림을 수신할 목적으로 `Subscription`을 발행했는데, B가 `Subscription`을 가로채고 알림을 보내려 한다면 거부된다. `VapidKey`가 다르므로(A의 비밀키를 모르므로) 인증에 실패한다.        
    명세에 따르면 `Subscription`에는 `endpoint`라는 `URL`이 반드시 포함되어야 하며, `endpoint`는 **반드시 UNIQUE** 해야 한다.
    
```javascript
//sample Subscription
{
    "endpoint": "https://fcm.googleapis.com/fcm/send/..",
    "expirationTime": 12345,
    "keys": {
        "p256dh": "p256dhkey...",
        "auth": "auth-info"
    }
}
```
## Scenario

### Client

[1.]() Application Server로부터 Web Push를 받을 것인지 결정한다. 브라우저에 권한을 부여하는 방식으로 이루어진다.

[2.]() push 이벤트를 처리할 ServiceWorker를 등록한다.
  
[3.]() Application Server로부터 `VapidKey`를 받는다.

[4.]() Push Service에 `VapidKey`를 전달하며 `Subscription`을 요청한다.

[6.]() Application Server에 `Subscription`을 전달한다. Application Server는 해당 End User와 `Subscription`을 내부적으로 처리해야 한다.(DB 저장 등)

### Application Server

[1.]() `VapidKey`를 준비한다. 

[2.]() End User의 `Subscription`을 저장한다.

[3.]() `VapidKey`, `Subscription`을 이용하여 encrypt를 수행한다.

[4.]() Push Service에 POST 요청을 보낸다.


