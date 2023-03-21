
<template>

    <v-data-table
        :headers="headers"
        :items="orderStatus"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'OrderStatusView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "id", value: "id" },
                { text: "productId", value: "productId" },
                { text: "qty", value: "qty" },
                { text: "customerId", value: "customerId" },
                { text: "status", value: "status" },
            ],
            orderStatus : [],
        }),
        async created() {
            var me = this;
            setInterval(function(){me.load()}, 1000);
        },
        methods: {
            async load() {
                var temp = await axios.get(axios.fixUrl('/orderStatuses'))

                temp.data._embedded.orderStatuses.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

                this.orderStatus = temp.data._embedded.orderStatuses;
            }
        }
    }
</script>

