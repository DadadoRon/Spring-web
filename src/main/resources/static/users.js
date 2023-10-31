export default {
    template: '#users',
    data() {
        return {
            user:  JSON.parse(localStorage.getItem("currentUser")),
        }
    },
    methods: {

    }
}
