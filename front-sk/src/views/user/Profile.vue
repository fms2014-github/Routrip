<template>
    <div class="profile-page">
        <div class="wrapD">
            <h2>My Profile</h2>
            <div class="profile-wrap">
                <UserPicture :userPicture="true" />
                <div class="user-info">
                    <HeaderComponent headerTitle="여기가 이메일" :mailIcon="true" />
                    <HeaderComponent headerTitle="여기가 닉네임" :profileIcon="true" rightText="수정" />
                </div>
            </div>
            <div class="wrap">
                <router-link v-bind:to="{ name: 'UserPost' }"><TabComponent tabTitle="글" :isActive="true"/></router-link>
                <router-link v-bind:to="{ name: 'UserComment' }"><TabComponent tabTitle="댓글" :isActive="true"/></router-link>
                <router-link v-bind:to="{ name: 'UserLike' }"><TabComponent tabTitle="좋아요" :isActive="true"/></router-link>
                <router-link v-bind:to="{ name: 'UserPeople' }"><TabComponent tabTitle="사람들" :isActive="true"/></router-link>
            </div>

            <div class="profile-tab-page">
                <router-view name="profile"></router-view>
            </div>
        </div>
    </div>
</template>

<script>
import UserApi from '../../apis/UserApi';
import HeaderComponent from '../../components/common/Header';
import UserPicture from '../../components/common/UserPicture';
import TabComponent from '../../components/common/Tab';
import '../../assets/css/profile.scss';
import '../../assets/css/style.scss';

export default {
    components: {
        HeaderComponent,
        UserPicture,
        TabComponent,
    },
};
</script>
