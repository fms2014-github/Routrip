<template>
    <button class="user-picture" @click="handleModal" v-if="userPicture"></button>
</template>

<script>
export default {
    name: 'userpicture',
    props: ['userPicture'],
    methods: {
        handleModal() {
            this.$swal({
                title: 'Are you sure?',
                text: 'Once deleted, you will not be able to recover this imaginary file!',
                icon: 'warning',
                buttons: true,
                dangerMode: true,
            }).then(willDelete => {
                if (willDelete) {
                    swal('Poof! Your imaginary file has been deleted!', {
                        icon: 'success',
                    });
                } else {
                    swal('Your imaginary file is safe!');
                }
            });
        },
    },
};
</script>
