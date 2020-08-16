# WEB PUSH

## 개요

[Push Api](https://developer.mozilla.org/ko/docs/Web/API/Push_API)를 이용한 푸시 기능을 지원한다.  
모바일 기반의 알림과 다른 점은, 브라우저 기반으로 알림이 전송되므로 PC에서도 알림을 송수신할 수 있다. 모바일 웹에서도 역시 가능하다.  
Push Api는 지금도 활발한 논의와 변경이 일어나고 있다. [Draft](https://www.w3.org/TR/push-api/)  
Push Api는 브라우저별로 구현이 다르다. 크롬은 FCM, Firebase Cloud Messaging, 파이어폭스는 Auto Push로 Push Api를 구현하고 있다. Safari는 ServiceWorker를 지원하지 않으므로 **Web Push를 사용할 수 없다**.

## Mechanism

Web Push에는 세 주체가 등장한다.
- End User  
    알림을 받을 최종 사용자.
    
- Application Server  
    End User에게 알림을 보내고자 하는 어플리케이션
  
- Push Service  
    Application Server로부터 알림을 요청받아 End User에게 알림을 전송한다. c.f) FCM, GCM, APN ...

Web Push 주요 용어

- [VapidKey](https://tools.ietf.org/html/draft-ietf-webpush-vapid-01)  
    Application Server가 스스로를 인증하기 위해 발행하는 키 쌍이다. End User는 Application Server로부터 VapidKey를 받아 Push Service에 전달한다. Push Service는 VapidKey를 검증하고 End User에게 Subscription을 발행한다.  
    Application Server가 Push Service에게 알림을 요청할 때, 스스로를 인증하기 위해 VapidKey를 이용한 encrypt를 수행한다.

- Subscription  
    End User로 알림을 보내기 위해 필요한 정보.  
    Application Server가 End User에게 알림을 보내고자 할 때, Subscription을 이용해서 Push Service에 요청하게 된다.  
    End User가 Push Service에게, Application Server로부터 Push를 받고자 한다는 의사를 전달하면, Push Service는 End User에게 Subscription, 구독정보를 생성하고 응답한다.      
    명세에 따르면 Subscription에는 `endpoint`라는 `URL`이 반드시 포함되어야 하며, `endpoint`는 **반드시 UNIQUE** 해야 한다.
    
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
    

