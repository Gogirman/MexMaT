import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);

const router = new Router({
    mode: 'history',
    routes: [
        {
            path: '/',
			name: 'index',
            component: () => import('../components/views/HomePage.vue')
        },
        {
            path: '/home',
			name: 'home',
            component: () => import('../components/views/HomePage.vue')
        },
        {
            path: '/todos',
			name: 'todos',
            component: () => import('../components/views/TodosPage.vue')
        },
        {
            path: '/todos/:id',
            name: 'todoId',
            component: () => import('../components/views/DescriptionPage.vue')
        },
        {
            path: '/addtodo',
            name: 'addtodo',
            component: () => import('../components/views/AddPage.vue')
        },
		{
			path: '/404',
			name: 'error',
			component:() => import('../components/views/NotFoundPage.vue')
		},
		{
			path: '*',
			name: 'allError',
			redirect: '/404'
		}
    ]
});
export default router;