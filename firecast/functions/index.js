const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();


exports.writeEvent = functions.database.ref('/events/{ID}').onWrite((snap, context) => {
    const event=snap.after.val()
    const eventId=snap.after.child('eventId').val()
    const userId=snap.after.child('userIdOfCreator').val()
    console.log('Event')
    console.log(event)
    console.log('EventID')
    console.log(eventId)
    console.log('UserID')
    console.log(userId)
    const promises=[]

    promises.push(admin.database().ref('/users_going_events/' + userId + "/" + eventId).set(event))
    promises.push(admin.database().ref('/users_events/'+userId+'/'+eventId).set(event))
    promises.push(admin.database().ref('/event_users/'+eventId+'/'+userId+'/isComming').set(true))

    return Promise.all(promises)

})

exports.updateEvent = functions.database.ref('/events/{ID}').onUpdate((snap, context) => {
    const event=snap.after.val()
    const eventId=snap.after.child('eventId').val()
    const userId=snap.after.child('userIdOfCreator').val()
    const promises=[]


    promises.push(admin.database().ref('/users_events/'+userId+'/'+eventId).set(event))

    promises.push(admin.database().ref('/event_users/').child(eventId).once('value',(snapshot)=>{
        snapshot.forEach((childSnapshot)=>{
            promises.push(admin.database().ref('/users_going_events/' + childSnapshot.key + "/" + eventId).set(event))
        })
    })
    )

    return Promise.all(promises)

})

exports.deleteEvent = functions.database.ref('/events/{ID}').onDelete((snap,context) =>{
    var eventID=context.params.ID
    var userId=snap.child('userIdOfCreator').val()
    console.log(userId)
    var promises=[]


    promises.push(
        admin.database().ref('/event_users/'+eventID).once('value',(snapshot)=>{
        var map={}
        snapshot.forEach((childSnapshot)=>{
            map['/users_going_events/' + childSnapshot.key + "/" + eventID]=null
        })

        map['/event_users/'+eventID]=null
        map['/users_events/' + userId + "/" + eventID]=null

        promises.push(
            admin.database().ref().update(map)
        )
    }))

    return Promise.all(promises)
})

exports.deleteUser = functions.database.ref('/users/{ID}').onDelete((snap, context) => {
    console.log(context.params.ID)
    var userId = context.params.ID;
    const promises = []

    console.log(userId + " Userdata")


    promises.push(
        admin.database().ref('/users_going_events/' + userId).once('value', (snapshot) => {
            snapshot.forEach((childSnapshot) => {
                promises.push(admin.database().ref('/event_users/' + childSnapshot.key + '/' + userId).remove())
            })
            snapshot.ref.remove()

            promises.push(
                admin.database().ref('/users_events/' + userId).once('value', (snapshot) => {
                    console.log("firs Key" + snapshot.key)
                    snapshot.forEach((childSnapshot) => {
                        promises.push(admin.database().ref('/events/' + childSnapshot.key).remove())
                            //promises.push(admin.database().ref('/event_users/' + childSnapshot.key).remove())
                            //promises.push(admin.database().ref('/users_going_events/'+userId+"/" + childSnapshot.key).remove())
                    })
                    snapshot.ref.remove()
                })
            )

        })
    )







    //promises.push(admin.database().ref('/users_events/' + userId).remove())
    //promises.push(admin.database().ref('/users_going_events/' + userId).remove())

    return Promise.all(promises)


});