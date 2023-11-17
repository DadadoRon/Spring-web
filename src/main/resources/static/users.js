export default {
    template: '#users',
    data() {
        return {
            user:  JSON.parse(localStorage.getItem("currentUser")),
            products:[],
            cards: [
                { title: 'Сертификат', src: 'img/n1.png', flex: 3,  price: 70 },

            ],
            mainContentNumber: 1,
        }
    },
    methods: {
        toProducts() {
            this.mainContentNumber = 1
        },
        toGoods() {
            this.mainContentNumber = 2
        },
        toPublications() {
            this.mainContentNumber = 3
        }

    },
    async created() {
        const response = await axios.get('/products')
        this.products = response.data
        for (let i = 0; i < this.products.length; i++) {
            this.products[i].reveal = false;
        }
    }
}
