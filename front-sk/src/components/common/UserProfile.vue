<template>
  <div class="flex-1 px-5">
            <div class="max-w-sm rounded overflow-hidden shadow-lg">
                <img class="w-full" :src="image">
                <div class="px-6 px-4" >
                    <div class="relative">
                        <div class="absolute pin-l pin-t bg-white border border-pear-dark rounded-full" style="...">
                            <img class="block rounded-full" :src="avatar" width="30">
                        </div>
                    </div>
                    <div class="font-bold text-xl mb-2">{{title}}타이틀</div>
                    <p class="text-grey-darker text-base">
                        {{description}}
                    </p>
                </div>
            </div>
        </div>
</template>

<script>

export default {
    props:{
        description:{
            type:"String",
        },
        title:"",
        image:"",
        avatar:"",
    },
    data(){
            
    }
}
</script>
