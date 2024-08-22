export default {
    template: '#resetPassword',
    data() {
        return {
            showPassword: false,
            newPassword: '',
            token: this.$route.query.token
        }
    },

    computed: {
        passwordFieldType() {
            return this.showPassword ? 'text' : 'password';
        },
    },

    methods: {
        togglePasswordVisibility() {
            this.showPassword = !this.showPassword;
        },

        async resetPassword() {
            try {
                await axios.post('/api/v1/common/users/password/init', {
                    token: this.token,
                    newPassword: this.newPassword
                }, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
                alert("Password has been successfully reset.");
                this.$router.push({path: '/login'})
            } catch (e) {
                alert("Failed to reset password. The token may be invalid or expired.");
            }
        }
    }
}