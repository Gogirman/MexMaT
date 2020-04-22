<template>
    <div class="container">
        <b-alert show variant="danger">
            {{ getError }}
        </b-alert>
    </div>
</template>

<script>
    export default {
        name: "NotFound",
        computed: {
            getError() {
                return this.$store.getters.notFoundError;
            }
        },
		created() {
            this.$store.dispatch('SET_ERROR', {type: 'notFound', error: '404 Page Not Found'});
        }
    }
</script>