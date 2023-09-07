export default {
    template: '#edit',
    data() {
        return {
            userId: this.$route.params.id,
            user: {}
        }
    },
    methods: {
        async update() {
            await axios.put('/users', this.user)
            this.$router.push({path: '/admins'})
        },
        async remove() {
            await axios.delete('/users/' + this.user.id)
            this.$router.push({path: '/admins'})
        }
    },
    async created() {
        const response = await axios.get('/users/' + this.userId)
        this.user = response.data
    }
}
