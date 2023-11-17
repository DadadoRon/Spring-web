
export default {
    template: '#first',
    data() {
        return {
            products:[],
            cards2: [
                { title: 'Домашний уход за кожей вокруг глаз', src: 'img/n1.png', flex: 3,  description: 'vdnvkn' },
                { title: 'Favorite road trips', src: 'img/n2.jpg', flex: 3 },
                { title: 'Best airlines', src: 'img/n3.jpg', flex: 3 },
                { title: 'Best airlines', src: 'img/n3.jpg', flex: 3 },
                { title: 'Best airlines', src: 'img/n3.jpg', flex: 3 },
                { title: 'Best airlines', src: 'img/n3.jpg', flex: 3},
            ],
            links: [
                'Home',
                'About Us',
                'Team',
                'Services',
                'Blog',
                'Contact Us',
            ],
            tab: null,
            mainContentNumber: 1,
        }
    },
    methods: {
        create(){
            this.$router.push({path: '/login'})
        },
        login(){
            this.$router.push({path: '/login'})
        },
        first(){
            this.$router.push({path: '/'})
        },

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
