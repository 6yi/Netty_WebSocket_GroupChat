<template>
	  <transition name="slide-fade">
		
		<div id="chat" v-if="!needLogin">
			  <div id="numbers">当前在线人数:{{numbers}}</div>
		<audio id="bgm"  >
	      <source src="../assets/10546.mp3" type="audio/mpeg"/>
		</audio>
	  
    <div id="group">
		<div class="officeGroup">
			<img class="chatImg" src="../assets/logo.png">
			<p class="chatName">官方群组</p>
			</div>
</div>

	<div id="chating">
		<div id="messageChat" ref="MsgChat">		
		</div>
	
		
		<div class="container" v-show="Emoji" id="emoji_chat">
		    <picker
		      :include="['people']"  
		      :showSearch="false"   
		      :showPreview="false" 
		      :showCategories="false"
		      @select="addEmoji"
		    />
		  </div>
		  <div id="img_emoji"><img v-on:click="selectEmoji" style="height: 20px;" src="../assets/img/表情.png"></div>
		<el-input class="inputChat" @keyup.enter.native="websocketsend(userName,'all',1,textarea2)"
		  type="textarea" maxlength="900"
		  :autosize="{ minRows: 3, maxRows: 6}"
		  placeholder="请输入内容"
		  v-model="textarea2">
		</el-input>
		<el-button  v-on:click="websocketsend(userName,'all',1,textarea2)" class="msgBtn" type="success"><font style="font-size: 15px;">发送</font></el-button>	
	</div>
	
	
  </div>
 
 </transition>
</template>

<script>
import Scroll from '../base/scroll/scroll.vue'
import {mapState} from 'vuex'
import { Picker } from "emoji-mart-vue";

  export default {
    name: 'App',
	computed:{
			...mapState(['needLogin','userName']),
			listData(){
				return this.$store.state.needLogin
			},
	},
	components:{
		Picker,
		Scroll
	},
	data(){
		return{
			numbers:'',
			Emoji:false,
			textarea2: '',
			websock:'',
			
			heart:3,
		
		}
	},
	methods:{
		selectEmoji(){
			if(this.Emoji){
				this.Emoji=false
			}else{
				this.Emoji=true
			}
			
		},
		 start(){
		        var self = this;
		         var iCount=setInterval(function(){
		             if (self.websock.readyState == 1&&self.heart>0) {
						 var sendMsg="{'formUser':'"+self.userName+"','sendUser':'"+self.userName+"','state':'"+9+"','content':'"+""+"'}"
							self.websock.send(sendMsg)
							console.log("心跳包:"+self.heart)
							self.heart=self.heart-1
		                }else{
							console.log("连接断开,进行重连")
							self.websock.onclose;
							self.initWebSocket();
							clearInterval(iCount);
							return;
		                }
		            }, 5000)
		        },
		
		 addEmoji(e) {
			 if(this.textarea2.length<900)
		      this.textarea2 += e.native;
			  this.Emoji=false
		    },
	
		initWebSocket(){ //初始化weosocket
		   const wsuri = `ws://59.110.173.180:7000/websocket`
		   this.websock = new WebSocket(wsuri);
		   this.websock.onmessage = this.websocketonmessage;
		   this.websock.onopen = this.websocketonopen;
		   this.websock.onerror = this.websocketonerror;
		   this.websock.onclose = this.websocketclose;
		   this.heart=3;
			
		 },
		 websocketonopen(){ //连接成功
		  this.websocketsend(this.userName,this.userName,0,this.userName+"连接上线~~")
		  this.heart=3;
		  this.start();
		 },
		 websocketonerror(){//连接建立失败重连
		   this.initWebSocket()
		 },
		 websocketonmessage(e){
				let _this = this //数据接收
				console.log(e)
				const redata = JSON.parse(e.data);
				if(redata.state!=9){
				if(redata.state!=8888){
					if(redata.sendUser=='all'&&redata.formUser!=this.userName){
						var a=document.createElement("div");
						a.className="msgOther"
						a.innerHTML=redata.content
						var b=document.createElement("div");
						b.className="otherName"
						b.innerHTML=redata.formUser+":"
						var c=document.createElement("div");
						c.appendChild(b)
						c.appendChild(a)
						this.$refs.MsgChat.appendChild(c)
						document.getElementById("bgm").play();
						let div = document.getElementById('messageChat');
						div.scrollTop = div.scrollHeight;
					}
				}else{
					this.numbers=redata.content
				}
				
				}else{
					this.heart=3;
					console.log("心跳包回复:"+this.heart)
				}	
		 },
		 websocketsend(formUser,sendUser,state,content){//数据发送
			var sendMsg="{'formUser':'"+formUser+"','sendUser':'"+sendUser+"','state':'"+state+"','content':'"+content+"'}"
			this.textarea2=''
			this.websock.send(sendMsg)
			var a=document.createElement("div");
			a.className="msgMe"
			a.innerHTML=content
			this.$refs.MsgChat.appendChild(a)
			 let div = document.getElementById('messageChat');
			 div.scrollTop = div.scrollHeight;
		 },
		 websocketclose(e){  //关闭
		   console.log('断开连接', e)
		 }
	},
	created(){
		
		},
	watch:{
		listData(newLogin, old){
			if(newLogin==false){
				this.initWebSocket();
				
			}
		}
	}
  }
</script>

<style>
	#numbers{
		font-size: 20px;
		position: fixed;
		top: 4%;
		left: 40px;
	}
	.otherName{
		width: 30%;
		margin-left: -7%;
	}
	.msgOther{
		font-size: 18px;
		background-color:#DDD5D5;
		padding: 5px;
		margin-top: 5px;
		margin-bottom: 5px;
		margin-left: 3%;
		width: 30%;
		border-radius: 15px;
		}
	.msgMe{
		font-size: 18px;
		/* float: right; */
		background-color: #42B983;
		padding: 5px;
		margin-top: 5px;
		margin-bottom: 5px;
		margin-left: 65%;
		right: 0px;
		width: 30%;
		border-radius: 15px;
		/* position: relative; */
		
		
	}

	#messageChat{
		overflow: auto;
		/* background-color: #2C3E50; */
		position: absolute;
		   top: 10px;
		   left: 0px;
		 height:  550px;
		 width: 100%;
	}
	
	#img_emoji{
		position: absolute;
		left: 5px;
		bottom: 10px;
	}
	
	#emoji_chat{
		position: absolute;
		bottom: 143px;
		z-index: 99;
		
	}
	
	.emoji-mart[data-v-7bc71df8] {
	  font-family: -apple-system, BlinkMacSystemFont, "Helvetica Neue", sans-serif;
	  display: -ms-flexbox;
	  display: flex;
	  -ms-flex-direction: column;
	  flex-direction: column;
	  height: 420px;
	  color: #ffffff !important;
	  border: 1px solid #d9d9d9;
	  border-radius: 5px;
	  background: #fff;
	}
	
	.text {
	  display: block;
	  margin: 0 auto;
	  margin-bottom: 10px;
	  width: 400px;
	  resize: none;
	  box-sizing: border-box;
	  padding: 5px 10px;
	  border-radius: 8px;
	}
	
	.text-place {
	  height: 80px;
	  box-sizing: border-box;
	  padding: 5px 10px;
	  border-radius: 8px;
	  background: #ddd5d5;
	}
	
	.text-place p {
	  display: flex;
	  align-items: center;
	  margin: 0;
	}
	
	.msgBtn{
		position: absolute;
		bottom: 3px;
		right: 5px;
		height: 5%;
		
		}
	.inputChat{
		position: absolute;
		bottom: 43px;
		left: 5px;
		width: 98%;
	}
	.chatImg{
		position: absolute;
		width: 13%;
		left: 7px;
	}
	.chatName{
		font-size: 20px;
		position: absolute;
		  top: -15px;
		  left: 75px;
		   
	}
	.officeGroup{
		position: relative;
		background-color: #FFFFFF;
		text-align: center;
		/* margin-top:-14px; */
		margin-bottom: 3px;
		left: 0px;
		height: 8%;
		width: 100%;
	}
	#chat{
		background-color: #2C3E50;
		height: 700px;
		width: 1000px;
		 position: fixed;
		    top: 0px;
		    left: 0px;
		    right: 0px;
		    bottom: 0px;
		    margin: auto;
	}
	#chating{
		background-color: #FFFFFF;
		position: absolute;
		 top: 0px;
		 right: 0px;
		 height: 100%;
		 width: 60%;
	}
	#group{
		background-color: aliceblue;
		position: absolute;
		   top: 0px;
		   left: 0px;
		 height: 100%;
		 width: 40%;
		 
	}
	
.slide-fade-enter-active {
  transition: all .3s ease;
}
.slide-fade-leave-active {
  transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}
.slide-fade-enter, .slide-fade-leave-to
/* .slide-fade-leave-active for below version 2.1.8 */ {
  transform: translateX(10px);
  opacity: 0;
}
</style>
