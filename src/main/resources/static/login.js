
export default {
    template: '#login',
    data() {
        return {
            userEmail: '',
            userPassword: '',
        }
    },
    methods: {
        async login() {

                const authToken = 'Basic ' + btoa(`${this.userEmail}:${this.userPassword}`)
                var response
                try {
                     response = await axios.get('/api/v1/users/profile', {
                        headers: {
                            Authorization: `${authToken}`,
                            'Content-Type': 'application/json'
                        }
                    });
                } catch (e) {
                    alert("User with such email and password doesn't exist")
                    return
                }

                const user = response.data;

                localStorage.setItem("currentUser", JSON.stringify(user))
                localStorage.setItem("authToken", authToken)

                if (user.role === "USER") {
                    this.$router.push({ path: '/users' });
                } else {
                    this.$router.push({ path: '/admins' });
                }



            // alert("User with such email and password doesn't exist");

        },

        registration(){
            this.$router.push({path: '/create'})
        },
        first(){
            this.$router.push({path: '/'})
        },
    },

 }
