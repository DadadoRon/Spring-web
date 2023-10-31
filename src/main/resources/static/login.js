
export default {
    template: '#login',
    data() {
        return {
            userEmail: '',
            userPassword: '',
        }
    },
    methods: {
        async login(){
            const response = await axios.get('/users')
            const users = response.data
            let isUserExists = false
            users.forEach((user) => {
                if (user.email === this.userEmail && user.password === this.userPassword) {
                    isUserExists = true
                    localStorage.setItem("currentUser", JSON.stringify(user))
                    if (user.role === "ADMIN") {
                        this.$router.push({path: '/admins'})
                    } else {
                        this.$router.push({path: '/users'})
                    }
                }
            })
            if (!isUserExists) {
                alert("User with such email and password doesn't exist");
            }
        },
        registration(){
            this.$router.push({path: '/create'})
        },
        first(){
            this.$router.push({path: '/'})
        },
    },

 }
