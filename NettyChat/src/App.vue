<template>
  <div id="app">
	<transition name="fade">
	<div id="loginForm" v-show="needLogin">
		<p style="font-size: 25px;">输入名称进入聊天室</p>
		<el-form :model="numberValidateForm" ref="numberValidateForm" label-width="100px" class="demo-ruleForm input_name" >
		  <el-form-item 
		    label="名称"
		    prop="userName"
		    :rules="[
		      { required: true, message: '名称不能为空'},
		    ]">
		    <el-input type="userName" v-model="numberValidateForm.userName" autocomplete="off"></el-input>
		  </el-form-item>
		  <el-form-item id="btn">
			 <el-button  v-loading.fullscreen.lock="fullscreenLoading"  type="success" @click="submitForm('numberValidateForm')">进入</el-button>
		  </el-form-item>
		</el-form>
	</div>
    </transition>
	<chat></chat>
		
  </div>
</template>


<script>
import {mapState} from 'vuex'
import chat from './components/chat.vue'
export default {
	name: 'App',
	components:{
		chat
	},
	computed:{
			 ...mapState(['needLogin']),
			
	},
	data(){
		return{
			 fullscreenLoading: false,
				success:false,
				  numberValidateForm: {
				          userName: ''
				       }
		}
	},
	methods: {
		     
		 submitForm(formName) {
		        this.$refs[formName].validate((valid) => {
		          if (valid) {
					var is =true
					this.fullscreenLoading = true;
					  setTimeout(() => {}, 1000);
					this.$axios.get('http://59.110.173.180:7071/login/'+this.numberValidateForm.userName).then(res=>{
						 this.fullscreenLoading =false;
						 if(res.data==0){
								this.$message.error('当前已有人使用该名称');
						 }else{
							  this.$store.commit("Login",this.numberValidateForm.userName)
						 }
					 })
					  if(is){
						 
					  }else{
						    this.$message.error('当前已有人使用该名称');
					  }
					
		          } else {
		            console.log('error submit!!');
		            return false;
		          }
		        });
		      },
		      resetForm(formName) {
		        this.$refs[formName].resetFields();
		      }
		    ,
 
    },
  mounted() {
		
  }

}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
  
}

.fade-enter-active, .fade-leave-active {
  transition: opacity .5s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
  opacity: 0;
}

#btn{	
		margin-left: -23%;
		margin-top: 30px;
}
.input_name{
	
	position: absolute;
	top: 90px;
	left: 16%;
	width: 70%;
}
  .el-select .el-input {
    width: 50px;
  }
  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
  
body{
	background: url(assets/img/2zrdI1g.jpg) no-repeat;
	  background-size:100%;
}
#loginForm{
	width: 40%;
	height: 250px;
	background-color: white;
	 position: fixed;
	    top: 0px;
	    left: 0px;
	    right: 0px;
	    bottom: 0px;
	    margin: auto;
	border-radius: 12px;	
	-moz-box-shadow:2px 2px 7px #333333; -webkit-box-shadow:2px 2px 7px #333333; box-shadow:2px 2px 7px #333333;
}
</style>
