export default {
    template: '#admins',
    data() {
        return {
            user: JSON.parse(localStorage.getItem("currentUser")),
            mainContentNumber: 1,
            productId: null,
            userId: null,
            dateTime: null,
            model: 'tab-2',
            dialog: false,
            dialogDelete: false,
            dialogWarning: false,
            itemsPerPage: 10,
            groupBy: [{key: 'role', order: 'asc'}],
            headers: [
                {title: '#', key: 'index'},
                {title: 'ID', key: 'id'},
                {title: 'First name', key: 'firstName'},
                {title: 'Last name', key: 'lastName'},
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
                {title: 'Image name', key: 'imageName'},
                {title: 'Actions', key: 'actions', sortable: false},
            ],
            headersUserAppointments: [
                {title: '#', key: 'index'},
                // {title: 'ID', key: 'id'},
                {title: 'Appointment time', key: 'dateTime'},
                {title: 'Name', key: 'product.name'},
                {title: 'First name', key: 'user.firstName'},
                {title: 'Last name', key: 'user.lastName'},
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
            return this.userAppointments.map((item, index) => ({
                ...item,
                index: index + 1,
                dateTime: moment(item.dateTime).format('LLL'),
            }));
        },
    },
    watch: {
        dialog(val) {
            val || this.close()
        },
        dialogDelete(val) {
            val || this.closeDelete()
        },

        dialogWarning(val) {
            val || this.closeWarning()
        },
    },
    methods: {
        getCurrentDateTime() {
            let currentDateTime = moment();
            return currentDateTime.format('YYYY-MM-DDTHH:mm');
        },
        getDateTimeSixMonthFromNow() {
            let futureDate = moment().add(6, 'months');
            return futureDate.format('YYYY-MM-DDTHH:mm');
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
            await ax.delete(`/api/v1/admin/users/${this.editedItem.id}`)
            const response = await ax.get('/api/v1/admin/users')
            console.log(response)
            this.users = response.data
            this.closeDelete()
        },

        async checkUserAppointments() {
            try {
                const userAppointmentsResponse = await ax.get(`/api/v1/admin/user-appointments/${this.editedItem.id}`);
                const response = userAppointmentsResponse.data;
                if (response) {
                    this.dialogWarning = true;
                    const resp = this.getCurrentAppointments(this.editedItem.id);
                } else {
                    await this.deleteUserConfirm();
                }
            } catch (error) {
                console.error("Error checking user appointments:", error);
            }
            this.closeDelete()
        },

        async deleteProductConfirm() {
            await ax.delete(`/api/v1/admin/products/${this.editedItem.id}`)
            const response = await axios.get('/api/v1/common/products')
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

        closeWarning() {
            this.dialogWarning = false
            this.$nextTick(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
                this.editedIndex = -1
            })
        },

        closeTable() {
            this.dialogTable = false
            this.$nextTick(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
                this.editedIndex = -1
            })
        },

        async saveUser() {
            if (this.editedIndex > -1) {
                const resp = await ax.put('/api/v1/admin/users', this.editedItem)
                console.log('put response', resp)

            } else {
                await ax.post('/api/v1/admin/users/create', this.editedItem)
            }
            const response = await ax.get('/api/v1/admin/users')
            this.users = response.data;
            console.log('response get all', this.users)
            this.close()
        },
        async saveProduct() {
            if (this.editedIndex > -1) {
                 await ax.put('/api/v1/admin/products', this.editedItem)
            } else {
                 await ax.post('/api/v1/admin/products', this.editedItem)
            }
            const response = await ax.get('/api/v1/common/products')
            this.products = response.data;
            this.close()
        },
        async saveUserAppointment() {
            const dateTime = this.editedItem.dateTime;
            const momentDateTime = moment(dateTime);
            if (this.editedIndex > -1) {
                const updatedAppointment = {
                    id: this.editedItem.id,
                    dateTime: momentDateTime,
                    userId: this.editedItem.userId,
                    productId: this.editedItem.productId,
                };
                await ax.put('/api/v1/admin/user-appointments',  updatedAppointment)
            } else {
                await ax.post('/api/v1/admin/user-appointments', {
                    dateTime: momentDateTime,
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
            const response = await ax.get('/api/v1/admin/users')
            this.users = response.data
        },
        async toProductsTable() {
            this.mainContentNumber = 3;
            const response = await ax.get('/api/v1/common/products')
            this.products = response.data
        },
        async toUserAppointmentsTable(){
            this.mainContentNumber = 4;
            const response = await ax.get('/api/v1/admin/user-appointments')
            this.userAppointments = response.data
            const userResponse = await ax.get('/api/v1/admin/users')
            this.users = userResponse.data
            const productResponse = await ax.get('/api/v1/common/products')
            this.products = productResponse.data
            this.closeWarning()
        },

        async search() {
            const response = await ax.post('/api/v1/admin/users/search', {
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






