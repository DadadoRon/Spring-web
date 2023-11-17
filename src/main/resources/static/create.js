export default {
    template: '#create',
    data() {
        return {
            // step: 1,
            user: {}
        }
    },
    // computed: {
    //     currentTitle () {
    //         switch (this.step) {
    //             case 1: return 'Sign-up'
    //             default: return 'Account created'
    //         }
    //     },
    // },
    methods: {
        async create(){
            await axios.post('/api/v1/users', this.user)
            this.$router.push({path: '/login'})
        },
    }
}
