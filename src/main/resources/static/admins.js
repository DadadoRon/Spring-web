export default {
    template: '#admins',
    data() {
        return {
            users: [],
        }
    },
    methods: {
        update: function (userId) {
            this.$router.push({path: '/edit/' + userId})
        },
        remove: async function (userId) {
            await axios.delete('/users/' + userId)
            const response = await axios.get('/users')
            this.users = response.data
        }
    },
    async created() {
        const response = await axios.get('/users')
        this.users = response.data
    }
}
