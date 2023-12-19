export default {
    template: '#users',
    data() {
        return {
            selectedDate: "00-00-00",
            selectedTime: null,
            tags: [
                '11:00:00',
                '12:00:00',
                '13:00:00',
            ],
            timelineItems: [
                {time: '5pm', title: 'New Icon', category: 'Mobile App'},
                {time: '5pm', title: 'New Icon', category: 'Mobile App'},
            ],
            open: ['Appointment'],
            appointments: [
                ['Current Appointments', 'mdi-calendar'],
                ['Appointment History', 'mdi-history'],
            ],
            currentappointments: [
                {title: 'Appointment 1', date: '2023-12-10', time: '13:00'},
                {title: 'Appointment 2', date: '2023-12-15', time: '13:00'},
                {title: 'Appointment 2', date: '2023-12-15', time: '13:00'},
                {title: 'Appointment 2', date: '2023-12-15', time: '13:00'},
            ],

            userAppointments: [],
            dialogVisible: false,
            dialogDelete: false,
            dialog: false,
            selectedProductId: null,
            user: JSON.parse(localStorage.getItem("currentUser")),
            products: [],
            cards: [
                {title: 'Сертификат', src: 'img/n1.png', flex: 3, price: 70},

            ],
            mainContentNumber: 1,
            editedIndex: -1,
            editedItem: {},
            defaultItem: {},

        }
    },
    watch: {
        dialogVisible(val) {
            val || this.close()
        },
        dialogDelete(val) {
            val || this.closeDelete()
        },
        dialog(val) {
            val || this.close()
        },
    },

    methods: {
        selectTime(time) {
            this.selectedTime = time;
        },
        changeSelectedTime(tag) {
            this.editedItem.time = tag;
        },
        openBookingDialog(productId) {
            this.dialog = true;
            this.selectedProductId = productId;
        },
        editItem(item) {
            this.editedIndex = item.index
            this.editedItem = Object.assign({}, item)
            this.dialogVisible = true
        },
        deleteItem(item) {
            this.editedIndex = item.index
            this.editedItem = Object.assign({}, item)
            this.dialogDelete = true
        },
        close() {
            this.dialogVisible = false
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

        async saveAndClose() {
            await ax.put('/api/v1/user-appointments', this.editedItem)
            const response = await ax.get(`/api/v1/user-appointments/personal`)
            this.userAppointments = response.data;
            this.close()
        },
        async saveAndCloseCreate() {
            await ax.post('/api/v1/user-appointments', {
                date: this.selectedDate,
                time: this.selectedTime,
                userId: this.user.id,
                productId: this.selectedProductId,
            });
            this.close()
        },


        async deleteUserAppointmentConfirm() {
            await ax.delete(`/api/v1/user-appointments/${this.editedItem.id}`);
            const response = await ax.get(`/api/v1/user-appointments/personal`)
            this.userAppointments = response.data
            this.closeDelete()
        },

        async toProducts() {
            this.mainContentNumber = 1
            const response = await ax.get('/api/v1/products')
            this.products = response.data
            for (let i = 0; i < this.products.length; i++) {
                this.products[i].reveal = false;
            }
        },
        async handleAppointmentClick(title) {
            if (title === 'Current Appointments') {
                const response = await ax.get(`/api/v1/user-appointments/personal`)
                this.userAppointments = response.data
                this.toCurrent();
            } else if (title === 'Appointment History') {
                this.toHistory();
            }
        },
        toGoods() {
            this.mainContentNumber = 2
        },
        toPublications() {
            this.mainContentNumber = 3
        },
        toCurrent() {
            this.mainContentNumber = 4
        },
        toHistory() {
            this.mainContentNumber = 5
        },
        toOrders() {
            this.mainContentNumber = 6
        },
        toWishes() {
            this.mainContentNumber = 7
        },
    },
    async created() {
    }
}
