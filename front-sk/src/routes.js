// Import Router
import Router from 'vue-router';

// Import Common Components
import Main from './views/jh/Main.vue';
import Detail from './views/jh/Detail.vue';
import Login from './views/user/Login.vue';
import Join from './views/user/Join.vue';
import JoinAuth from './views/user/JoinAuth.vue';
import ErrorPage from './views/error/Error.vue';
import PageNotFound from './views/error/PageNotFound.vue';

// Import User Service Components
import Profile from './views/user/profile/Profile.vue';
import ChangePassword from './views/user/ChangePassword.vue';
import FindEmailAndPassword from './views/user/FindEmailAndPassword.vue';

// Import Post Components
import UserPost from './views/user/profile/UserPost.vue';
import UserComment from './views/user/profile/UserComment.vue';
import UserLike from './views/user/profile/UserLike.vue';
import UserPeople from './views/user/profile/UserPeople.vue';
import WriteForm from './views/main/WriteForm.vue';
import PictureRegister from './views/user/profile/PictureRegister.vue';

// Import Etc Components
import Components from './views/Components.vue';

export const router = new Router({
    mode: 'history',
    routes: [{
        path: '/',
        name: 'Login',
        component: Login,
        children: [{
                path: 'user/join',
                name: 'Join',
                components: {
                    join: Join,
                },
            },
            {
                path: 'user/joinauth',
                name: 'JoinAuth',
                components: {
                    join: JoinAuth,
                },
            },
            {
                path: '/user/FindEmailAndPassword',
                name: 'FindEmailAndPassword',
                components: {
                    find: FindEmailAndPassword,
                },
            },
        ],
    },
    {
        path: '/profile',
        name: 'Profile',
        component: Profile,
        children: [{
                path: '',
                name: 'UserPost',
                components: {
                    profile: UserPost,
                },
            },
            {
                path: 'picture',
                name: 'PictureRegister',
                components: {
                    picture_register: PictureRegister,
                },
            },
            {
                path: 'comment',
                name: 'UserComment',
                components: {
                    profile: UserComment,
                },
            },
            {
                path: 'like',
                name: 'UserLike',
                components: {
                    profile: UserLike,
                },
            },
            {
                path: 'people',
                name: 'UserPeople',
                components: {
                    profile: UserPeople,
                },
            },
        ],
    },
        {
            path: '/user/ChangePassword',
            name: 'ChangePassword',
            component: ChangePassword,
        },
        {
            path: '*',
            name: 'PageNotFound',
            component: PageNotFound,
        },
        {
            path: '/error',
            name: 'ErrorPage',
            component: ErrorPage,
        },
        {
            path: '/WriteForm',
            name: 'WriteForm',
            component: WriteForm,
        },
        {
            path: '/components',
            name: 'Components',
            component: Components,
        },
        {
            path: '/main',
            name: 'Main',
            component: Main,
        },
        {
            path: '/detail',
            name: 'Detail',
            component: Detail,
        },
    ],
})