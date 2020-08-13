let messaging
let token

document.getElementById('token').addEventListener('click', async (e) => {
    if (!messaging) {
        const config = await (await fetch('/subs-config')).json()
        firebase.initializeApp(config)
        messaging = firebase.messaging()
        token = await messaging.getToken()
    }
    console.log(token)
    document.getElementById('token-info').innerText = token
})

document.getElementById('sub').addEventListener('click', async (e) => {
    if (!token) {
        alert('MUST GET TOKEN BEFORE SUBSCRIBE')
        return
    }
    const response = await fetch('/subscribe', {
        method: 'post',
        headers: {
            'content-type': 'application/json'
        },
        body: JSON.stringify({
            token
        })
    })
    if (response.status !== 201) {
        console.error('AN ERROR OCCURRED')
    } else {
        console.log('COMPLETE SUBSCRIBE')
        readyForPush()
    }
})

document.getElementById('push').addEventListener('click', async (e) => {
    const title = document.getElementById('title').value
    const body = document.getElementById('body').value

    fetch('/push', {
        method: 'post',
        headers: {
            'content-type': 'application/json'
        },
        body: JSON.stringify({
            title,
            body
        })
    })
})

async function readyForPush() {
    const permission = await Notification.requestPermission()
    if (permission !== 'granted') {
        console.log('Permission not granted')
        return
    }
    if (!'serviceWorker' in navigator) {
        console.log('ServiceWorker not Supported')
        return
    }
    navigator.serviceWorker.register('/firebase-messaging-sw.js')
    .then(registration => {
        messaging.onMessage(payload => {
            const {title, body} = payload.notification
            const options = {
                body,
                requireInteraction: 'true',
                actions: [{
                    title: 'OPEN',
                    action: 'goTab'
                }]
            }
            navigator.serviceWorker.ready
            .then(registration => {
                registration.showNotification(title,options)
            })
        })
    })
    
}

