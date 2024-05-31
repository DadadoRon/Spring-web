export default {
    template: '#users',
    data() {
        return {
            dateTime: null,
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
        getCurrentDateTime() {
            let currentDateTime = moment();
            return currentDateTime.format('YYYY-MM-DDTHH:mm');
        },
        getDateTimeSixMonthFromNow() {
            let futureDate = moment().add(6, 'months');
            return futureDate.format('YYYY-MM-DDTHH:mm');
        },
        formatDate(dateTime) {
            return moment(dateTime).format('LLLL');
        },
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
            const dateTime = this.editedItem.dateTime;
            const momentDateTime = moment(dateTime);
            const updatedAppointment = {
                id: this.editedItem.id,
                dateTime: momentDateTime,
                userId: this.editedItem.userId,
                productId: this.editedItem.productId,
            };
            await ax.put('/api/v1/user/user-appointments', updatedAppointment)
            const response = await ax.get(`/api/v1/user/user-appointments`)
            this.userAppointments = response.data;
            this.close()
        },
        async saveAndCloseCreate() {
            const dateTime = this.editedItem.dateTime;
            const momentDateTime = moment(dateTime);
            await ax.post('/api/v1/user/user-appointments', {
                dateTime: momentDateTime,
                userId: this.user.id,
                productId: this.selectedProductId,
            });
            this.close()
        },
        async deleteUserAppointmentConfirm() {
            await ax.delete(`/api/v1/user/user-appointments/${this.editedItem.id}`);
            const response = await ax.get(`/api/v1/user/user-appointments`)
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
                const response = await ax.get(`/api/v1/user/user-appointments`)
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
