
export default {
    template: '#first',
    data() {
        return {
            reveal: false,
            cards: [
                { title: 'Карбокситерапия', src: 'img/n1.png', flex: 4,  description: 'vdnvkn' },
                { title: 'Favorite road trips', src: 'img/n2.jpg', flex: 4 },
                { title: 'Best airlines', src: 'img/n3.jpg', flex: 4 },
                { title: 'Best airlines', src: 'img/n3.jpg', flex: 4 },
                { title: 'Best airlines', src: 'img/n3.jpg', flex: 4 },
                { title: 'Best airlines', src: 'img/n3.jpg', flex: 4 },
            ],
            reveals: [
                false, false,false, false, false, false
            ],
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

        toHome() {
            this.mainContentNumber = 1

        },
        toRecents() {
            this.mainContentNumber = 2
        },
        toFavorites() {
            this.mainContentNumber = 3
        }
    },
}
