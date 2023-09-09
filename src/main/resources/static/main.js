import login from './login.js';
import admins from './admins.js';
import create from './create.js';
import edit from './edit.js';
import users from './users.js';

const routes = [
    { path: '/', component: login },
    { path: '/admins', component: admins },
    { path: '/create', component: create },
    { path: '/edit/:id', component: edit },
    { path: '/users', component: users }
]

const router = VueRouter.createRouter({
    history: VueRouter.createWebHashHistory(),
    routes
})


const app = Vue.createApp({})
app.use(router)
app.mount('#main')