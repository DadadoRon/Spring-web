export default {
    template: '#admins',
    data() {
        return {
            user: JSON.parse(localStorage.getItem("currentUser")),
            mainContentNumber: 1,
            selectedDate: "00-00-00",
            selectedTime: null,
            productId: null,
            userId: null,
            tags: [
                '11:00:00',
                '12:00:00',
                '13:00:00',
                '14:00:00',
                '15:00:00',
                '16:00:00',
            ],
            model: 'tab-2',
            dialog: false,
            dialogDelete: false,
            itemsPerPage: 10,
            groupBy: [{key: 'role', order: 'asc'}],
            headers: [
                {title: '#', key: 'index'},
                {title: 'ID', key: 'id'},
                {title: 'First Name', key: 'firstName'},
                {title: 'Last Name', key: 'lastName'},
                {title: 'Email', key: 'email'},
                {title: 'Password', key: 'password'},
                {title: 'Role', key: 'role'},
                {title: 'Actions', key: 'actions', sortable: false},
            ],
            users: [],
            headersProducts: [
                {title: '#', key: 'index'},
                {title: 'ID', key: 'id'},
                {title: 'Name', key: 'name'},
                {title: 'Description', key: 'description'},
                {title: 'Price', key: 'price'},
                {title: 'ImageName', key: 'imageName'},
                {title: 'Actions', key: 'actions', sortable: false},
            ],
            headersUserAppointments: [
                {title: '#', key: 'index'},
                // {title: 'ID', key: 'id'},
                {title: 'Date', key: 'date'},
                {title: 'Time', key: 'time'},
                {title: 'Name', key: 'product.name'},
                {title: 'FirstName', key: 'user.firstName'},
                {title: 'LastName', key: 'user.lastName'},
                {title: 'Email', key: 'user.email'},
                {title: 'Actions', key: 'actions', sortable: false},
            ],
            products: [],
            userAppointments: [],
            editedIndex: -1,
            editedItem: {},
            defaultItem: {},
            firstNameSearchTerm: '',
            lastNameSearchTerm: '',
            emailSearchTerm: '',
        }
    },
    computed: {
        formTitle() {
            return this.editedIndex === -1 ? 'New Item' : 'Edit Item'
        },
        numberedItems() {
            return this.users.map((item, index) => ({...item, index: index + 1}));
        },
        numberedProducts() {
            return this.products.map((item, index) => ({...item, index: index + 1}));
        },
        numberedUserAppointments() {
            return this.userAppointments.map((item, index) => ({...item, index: index + 1}));
        },

    },
    watch: {
        dialog(val) {
            val || this.close()
        },
        dialogDelete(val) {
            val || this.closeDelete()
        },
    },
    methods: {
        selectTime(time) {
            this.selectedTime = time;
        },
        changeSelectedTime(tag) {
            this.editedItem.time = tag;
        },
        editItem(item) {
            this.editedIndex = item.index
            this.editedItem = Object.assign({}, item)
            this.dialog = true
        },
        deleteItem(item) {
            this.editedIndex = item.index
            this.editedItem = Object.assign({}, item)
            this.dialogDelete = true
        },
        async deleteUserConfirm() {
            await ax.delete(`/api/v1/users/${this.editedItem.id}`)
            const response = await ax.get('/api/v1/users')
            this.users = response.data
            this.closeDelete()
        },
        async deleteProductConfirm() {
            await ax.delete(`/api/v1/products/${this.editedItem.id}`)
            const response = await axios.get('/api/v1/products')
            this.products = response.data
            this.closeDelete()
        },
        async deleteUserAppointmentConfirm() {
            await ax.delete(`/api/v1/admin/user-appointments/${this.editedItem.id}`)
            const response = await ax.get('/api/v1/admin/user-appointments')
            this.userAppointments = response.data
            this.closeDelete()
        },
        close() {
            this.dialog = false
            this.$nextTick(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
                this.editedIndex = -1
            })
        },
        closeDelete() {
            this.dialogDelete = false
            this.$nextTick(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
                this.editedIndex = -1
            })
        },
        async saveUser() {
            if (this.editedIndex > -1) {
                await ax.put('/api/v1/users', this.editedItem)
            } else {
                await ax.post('/api/v1/users/create', this.editedItem)
            }
            const response = await ax.get('/api/v1/users')
            this.users = response.data;
            this.close()
        },
        async saveProduct() {
            if (this.editedIndex > -1) {
                 await ax.put('/api/v1/products', this.editedItem)
            } else {
                 await ax.post('/api/v1/products', this.editedItem)
            }
            const response = await ax.get('/api/v1/products')
            this.products = response.data;
            this.close()
        },
        async saveUserAppointment() {
            if (this.editedIndex > -1) {
                await ax.put('/api/v1/admin/user-appointments',  this.editedItem)
            } else {
                await ax.post('/api/v1/admin/user-appointments', {
                    date: this.editedItem.date,
                    time: this.editedItem.time,
                    userId:this.editedItem.userId,
                    productId: this.editedItem.productId,
                })
            }
            const response = await ax.get('/api/v1/admin/user-appointments')
            this.userAppointments = response.data;
            this.close()
        },
        toHome() {
            this.mainContentNumber = 1;
        },
        async toUsersTable() {
            this.mainContentNumber = 2;
            const response = await ax.get('/api/v1/users')
            this.users = response.data

        },
        async toProductsTable() {
            this.mainContentNumber = 3;
            const response = await ax.get('/api/v1/products')
            this.products = response.data

        },
        async toUserAppointmentsTable(){
            this.mainContentNumber = 4;
            const response = await ax.get('/api/v1/admin/user-appointments')
            this.userAppointments = response.data
            const userResponse = await ax.get('/api/v1/users')
            this.users = userResponse.data
            const productResponse = await ax.get('/api/v1/products')
            this.products = productResponse.data
        },
        async search() {
            const response = await ax.post('/api/v1/users/search', {
                lastName: this.lastNameSearchTerm,
                email: this.emailSearchTerm,
                firstName: this.firstNameSearchTerm,
            })
            this.users = response.data
        }
    },
    async created() {
    }
}






