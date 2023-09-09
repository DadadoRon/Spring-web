export default {
    template: '#create',
    data() {
        return {
            user: {}
        }
    },
    methods: {
        async create(){
            await axios.post('/users', this.user)
            this.$router.push({path: '/'})
        }
    }
}
