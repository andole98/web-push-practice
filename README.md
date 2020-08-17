# WEB PUSH

## Overview

[Push Api](https://developer.mozilla.org/ko/docs/Web/API/Push_API)를 이용한 푸시 기능을 지원한다. Push Api는 지금도 활발한 논의와 변경이 일어나고 있다. [Draft](https://www.w3.org/TR/push-api/)    
웹소켓과 같은 기술로도 알림을 구현할 수 있다. 그러나 웹소켓은 offline 상황에서 알림을 보낼 수 없다.    
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
    Application Server가 스스로를 인증하기 위해 발행하는 키 쌍이다. End User는 Application Server로부터 VapidKey를 받아 Push Service에 전달한다. Push Service는 VapidKey를 검증하고 End User에게 `Suubscription`을 발행한다.  
    Application Server가 Push Service에게 알림을 요청할 때, 스스로를 인증하기 위해 VapidKey를 이용한 encrypt를 수행한다.

- `Suubscription`  
    Application Server가 End User로 알림을 보내기 위해 필요한 정보.  
    Application Server가 End User에게 알림을 보내고자 할 때, `Suubscription`을 이용해서 Push Service에 요청하게 된다.  
    End User가 Push Service에게, Application Server로부터 Push를 받고자 한다는 의사를 전달하면, Push Service는 End User에게 `Suubscription`, 구독정보를 생성하고 응답한다.      
    명세에 따르면 `Suubscription`에는 `endpoint`라는 `URL`이 반드시 포함되어야 하며, `endpoint`는 **반드시 UNIQUE** 해야 한다.
    
```json
{
    "endpoint": "https://fcm.googleapis.com/fcm/send/..",
    "expirationTime": 12345,
    "keys": {
        "p256dh": "p256dhkey",
        "auth": "auth-info"
    }
}
```
## Scenario

1. End User는 Application Server로부터 Web Push를 받을 것인지 결정한다. 브라우저에 권한을 부여하는 방식으로 이루어진다.
1. End User는 Application Server로부터 `VapidKey`를 가져온다.
1. End User는 Push Service에 `VapidKey`를 전달하며 `Suubscription`을 요청한다.   
1. Push Service는 `VapidKey`의 유효성을 검사하고, 내부적으로 인증정보를 저장한 뒤 `Suubscription`을 전달한다.
1. End User는 Application Server에 `Suubscription`을 전달한다. Application Server는 해당 End User와 `Suubscription`을 내부적으로 처리해야 한다.(DB 저장 등)
1. Application Server가 알림을 보내려면, `Suubscription`을 이용하여 Push Service에 푸시를 요청한다. Push Service는 요청의 **Authorization** 헤더의 값으로 인증을 수행하고, 문제없다면 End User에게 알림을 Push한다.   
1. End User의 ServiceWorker는 push 이벤트를 받는다.

