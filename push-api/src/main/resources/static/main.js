document.getElementById('sub').addEventListener('click', async (e) => {
    const permission = await Notification.requestPermission()
    if (permission !== 'granted') {
        console.log("PERMISSION NOT GRANTED")
        return
    }

    if (!'serviceWorker' in navigator) {
        console.log("SERVICE WORKER NOT SUPPORTED")
        return
    }

    const publicKey = await (await fetch('/application-server-key')).text()
    console.log(publicKey)
    const register = await navigator.serviceWorker.register('/sw.js',{
        scope: '/'
    })
    const subscription = await register.pushManager.subscribe({
        userVisibleOnly: true,
        applicationServerKey: urlB64ToUint8Array(publicKey)
    })

    const response =await fetch('/subscribe', {
        method: 'post',
        headers: {
            'content-type': 'application/json'
        },
        body: JSON.stringify(subscription)
    })
    console.log(response)

    document.getElementById('sub-info').innerText = subscription
})

document.getElementById('push').addEventListener('click', async(event) => {
    const title = document.getElementById('title').value
    const body = document.getElementById('body').value

    await fetch('/pushAll', {
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


function urlB64ToUint8Array(base64String) {
    const padding = '='.repeat((4 - base64String.length % 4) % 4);
    const base64 = (base64String + padding)
      .replace(/\-/g, '+')
      .replace(/_/g, '/');
  
    const rawData = window.atob(base64);
    const outputArray = new Uint8Array(rawData.length);
  
    for (let i = 0; i < rawData.length; ++i) {
      outputArray[i] = rawData.charCodeAt(i);
    }
    return outputArray;
  }