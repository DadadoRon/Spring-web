export default {
    template: '#admins',
    data() {
        return {
            user:  JSON.parse(localStorage.getItem("currentUser")),
            mainContentNumber: 1,
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
            headersProducts: [
                { title: '#', key: 'index' },
                { title: 'ID', key: 'id' },
                { title: 'Name', key: 'name' },
                { title: 'Description', key: 'description' },
                { title: 'Price', key: 'price' },
                { title: 'ImageName', key: 'imageName' },
                { title: 'Actions', key: 'actions', sortable: false},

            ],
            products:[],
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
        numberedProducts() {
            return this.products.map((item, index) => ({ ...item, index: index + 1 }));
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
            const authToken = localStorage.getItem("authToken")
            await axios.delete(`/api/v1/users/${this.editedItem.id}`, {
                headers: {
                    Authorization: `${authToken}`,
                    'Content-Type': 'application/json'
                }
                }
            )
            const response = await axios.get('/api/v1/users', {
                headers: {
                    Authorization: `${authToken}`,
                    'Content-Type': 'application/json'
                }
            })
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
            const authToken = localStorage.getItem("authToken")
            if (this.editedIndex > -1) {
                await axios.put('/api/v1/users', this.editedItem, {
                    headers: {
                        Authorization: `${authToken}`,
                        'Content-Type': 'application/json'
                    }
                })
            } else {
                await axios.post('/api/v1/users', this.editedItem, {
                    headers: {
                        Authorization: `${authToken}`,
                        'Content-Type': 'application/json'
                    }
                })
            }
            const response = await axios.get('/api/v1/users', {
                headers: {
                    Authorization: `${authToken}`,
                    'Content-Type': 'application/json'
                }
            })
            this.users = response.data;
            this.close()
        },
        toHome() {
            this.mainContentNumber = 1;
        },
        async toUsersTable() {
            this.mainContentNumber = 2;
            const authToken = localStorage.getItem("authToken")
            const usersResponse = await axios.get('/api/v1/users' , {
                headers: {
                    Authorization: `${authToken}`,
                    'Content-Type': 'application/json'
                }
            })
            this.users = usersResponse.data

        },
        async toProductsTable() {
            this.mainContentNumber = 3;
            const productsResponse = await axios.get('/api/v1/products')
            this.products = productsResponse.data
            for (let i = 0; i < this.products.length; i++) {
                this.products[i].reveal = false;
            }

        },
    },
    async created() {

    }
}





