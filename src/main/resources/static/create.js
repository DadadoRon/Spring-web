
export default {
    template: '#create',
    data() {
        return {
            confirmPassword: '',
            valid: false,
            user: {},
            firstNameRules: [
                v => !!v || 'Name is required',
                v => (v && v.length >= 2 && v.length <= 50) || 'First name must be between 2 and 50 characters long',
            ],
            lastNameRules: [
                v => !!v || 'Name is required',
                v => (v && v.length >= 2 && v.length <= 50) || 'Last name must be between 2 and 50 characters long',
            ],
            emailRules: [
                v => !!v || 'Email is required.',
                v => (/.+@.+\..+/.test(v)) || 'Email must be valid.',
            ],
            passwordRules: [
                v => !!v || 'Password is required',
                v => (v.length >= 8) || 'Password must be at least 8 characters long.',
                v => (/^(?=.*\d)(?=.*[@$!%*?&])[\d@$!%*?&]+$/.test(v)) || 'Password must contain at least one digit and one special character.',
            ],
            confirmPasswordRules: [
                (v) => v === this.user.password || 'Passwords do not match.',
            ],
        }
    },
    methods: {
        async validateAndCreate() {
            const {valid} = await this.$refs.form.validate();
            if (valid) {
                await axios.post('/api/v1/users', this.user);
                this.$router.push({path: '/login'});
                alert('User created successfully');
            } else {
                alert('Form is not valid');
            }
        },
        reset () {
            this.$refs.form.reset()
        },
    }
}
