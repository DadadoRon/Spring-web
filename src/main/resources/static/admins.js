export default {
    template: '#admins',
    data() {
        return {
            model: 'tab-2',
            dialog: false,
            dialogDelete: false,
            itemsPerPage: 10,
            groupBy: [{ key: 'role', order: 'asc' }],
            headers: [
                { title: '#', key: 'index' },
                { title: 'ID', key: 'id' },
                { title: 'First Name', key: 'firstName' },
                { title: 'Last Name', key: 'lastName' },
                { title: 'Email', key: 'email' },
                { title: 'Password', key: 'password' },
                { title: 'Actions', key: 'actions', sortable: false},

            ],
            users: [],
            editedIndex: -1,
            editedItem: {
            },
            defaultItem: {
            },
        }
    },
    computed: {
        formTitle () {
            return this.editedIndex === -1 ? 'New User' : 'Edit Item'
        },
        numberedItems() {
            return this.users.map((item, index) => ({ ...item, index: index + 1 }));
        },
    },
    watch: {
        dialog (val) {
            val || this.close()
        },
        dialogDelete (val) {
            val || this.closeDelete()
        },
    },
    methods: {
        editItem (item) {
            this.editedIndex = item.index
            this.editedItem = Object.assign({}, item)
            this.dialog = true
        },
        deleteItem (item) {
            this.editedIndex = item.index
            this.editedItem = Object.assign({}, item)
            this.dialogDelete = true
        },
        async deleteItemConfirm() {
            // this.users.splice(this.editedIndex - 1, 1)
            // this.closeDelete()
            await axios.delete(`/users/${this.editedItem.id}`)
            const response = await axios.get('/users')
            this.users = response.data
            this.closeDelete()
        },

        close () {
            this.dialog = false
            this.$nextTick(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
                this.editedIndex = -1
            })
        },

        closeDelete () {
            this.dialogDelete = false
            this.$nextTick(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
                this.editedIndex = -1
            })
        },
        async save() {
            if (this.editedIndex > -1) {
                await axios.put('/users', this.editedItem)
            } else {
                await axios.post('/users', this.editedItem)
                }
            const response = await axios.get('/users')
            this.users = response.data;
            this.close()
        },

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




