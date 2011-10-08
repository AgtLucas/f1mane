<%
    response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
    response.setHeader("Pragma","no-cache"); //HTTP 1.0
    response.setDateHeader ("Expires", -1); //evita o caching no servidor proxy
%>

<html>
<head>
<title>F1-Mane</title>
<META NAME="description"
	CONTENT="Formula 1,FIA,F1,Esportes,Sport,Jogo,Game,Manager,Gerenciamento">

<style type="text/css">
div {
	cursor: pointer;
	font-family: sans-serif;
}

A:link {text-decoration: none; 
		color: #0084B4;}
A:visited {text-decoration: none;
		color: #0084B4;} 
A:active {text-decoration: none;
		color: #0084B4;}
A:hover {text-decoration: underline overline; color: black;}

#link {
	border-style: solid;
	border-width: 1px;
	border-color: #B8CFE5;
	text-align: left;
	padding: 3px;
	padding-left: 10px;
	padding-right: 10px;
	cursor: pointer;
	font-family: sans-serif;
	color: #0084B4;
	text-decoration: none;
}

#pop{
     display:none;
     position:absolute;
	 top:28%;
	 left:30%;
	 margin-left:-150px;
	 margin-top:-100px;
	 padding-left: 20px;
	 padding-right: 20px;
	 padding:10px;
	 width:550px;
	 height:450px;
	 border:1px solid #B8CFE5;
    }

img {
	border-style: none;
	border-width: 0px;
	border-color: #5C5E5D;
	padding: 10px;
	padding-left: 0px;
	padding-right: 0px;
}

#title {
	border-style: none;
	text-align: left;
	font-family: Arial, sans-serif;
	font-size: 24px;
	font-weight: bold;
	line-height: 24px;
	height: 100px;
}

#adds {
	font-family: Arial, sans-serif;
	font-size: 24px;
	font-weight: bold;
	position: relative;
	float :left;
}

#main{
	padding: 3px;
	padding-left: 20px;
	padding-right: 20px;
	border-style: none;
	position: relative;
	float: right;	
}

#shots{
	border-style: solid;
	border-width: 1px;
	border-color: #B8CFE5;
	text-align: center;
	padding: 3px;
	padding-left: 20px;
	padding-right: 20px;
	cursor: pointer;
	font-family: sans-serif;
}

#shotsPromo{
	border-style: solid;
	border-width: 1px;
	border-color: #B8CFE5;
	text-align: center;
	padding: 15px;
}

#description {
	color: #666666;
	font-size: 13px;
	font-style: italic;
}
</style>
<script type="text/javascript" src="highslide/highslide-full.js"></script>
<link rel="stylesheet" type="text/css" href="highslide/highslide.css" />

<!--
	2) Optionally override the settings defined at the top
	of the highslide.js file. The parameter hs.graphicsDir is important!
-->

<script type="text/javascript">
	
	hs.graphicsDir = 'highslide/graphics/';
	hs.align = 'center';
	hs.transitions = ['expand', 'crossfade'];
	hs.outlineType = 'rounded-white';
	hs.fadeInOut = true;
	hs.dimmingOpacity = 0.75;

	// define the restraining box
	hs.useBox = true;
	hs.width = 800;
	hs.height = 600;

	// Add the controlbar
	hs.addSlideshow({
		//slideshowGroup: 'group1',
		interval: 5000,
		repeat: false,
		useControls: true,
		fixedControls: 'fit',
		overlayOptions: {
			opacity: 1,
			position: 'bottom center',
			hideOnMouseOut: true
		}
	});


	
</script>

</head>
<body>
<iframe src="http://www.facebook.com/plugins/likebox.php?href=http%3A%2F%2Fwww.facebook.com%2Fapps%2Fapplication.php%3Fid%3D196453917042292&amp;width=292&amp;colorscheme=light&amp;show_faces=false&amp;stream=false&amp;header=false&amp;height=62" scrolling="no" frameborder="0" style="border:none; overflow:hidden; width:292px; height:62px;" allowTransparency="true"></iframe>
<table >
	<tr> 
	<td>
	<div id="adds">
		<script type="text/javascript"><!--
		google_ad_client = "pub-1471236111248665";
		/* 120x600, criado 14/06/10 */
		google_ad_slot = "5219714006";
		google_ad_width = 120;
		google_ad_height = 700;
		//-->
		</script>
		<script type="text/javascript"
		src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
		</script>
	</div>
	</td>
	<td>
	<div id="main">
		<div id="title">	
			<span >F1-Mane
				<h1 id="description">MANager & Engineer</h1>
			</span>
			<a id="link" style="position: absolute; left: 150px; top: 10px;font-size: 16px;"
				href="f1mane_en.jsp">English</a>
			<a id="link" style="position: absolute; left: 250px; top: 10px;font-size: 16px;"
				href="http://sowbreira.appspot.com/" 
				target="_BLANK">Site Autor</a>
			<a id="link" style="position: absolute; left: 370px; top: 10px;font-size: 16px;"
				href="mailto:sowbreira@gmail.com" 
				target="_BLANK">Reportar Bugs</a>		
			<a id="link" style="position: absolute; left: 220px; top: 50px;font-size: 16px;"
				href="http://www.java.com/" 
				target="_BLANK">Instale o Java</a>
			<a id="link" style="position: absolute; left: 370px; top: 50px;font-size: 16px;"
				style="text-align: right;" onclick="document.getElementById('pop').style.display='block';">
				Como Jogar</a>		
			<br>					
		</div>
		<div id="pop" style="background-color: white;">
	    	F1 Mane Jogo de estrategia de corrida de F1
	    	<a href="#" style="position:absolute; left:92%" onclick="document.getElementById('pop').style.display='none';">[X]</a>
			<p style="color: #0084B4;">
				Dentro do jogo:	
			</p>
			<UL>
			   <LI >Seu piloto esta na celula azul na tabela a direita</LI>
			   <LI >Pode-se acompanhar a estrategia dos outros pilotos na tabela</LI>			   
			   <LI >Use F1,F2,F3 para controlar o Giro do motor</LI>
			   <LI >Use F4 para alternar rapidamente entre condu��o agressiva e normal</LI>
			   <LI >Use F5,F6,F7 para controlar a agressividade do piloto</LI>
			   <LI >Use F8 para desligar o modo de tra�ado automatico </LI>
			   <LI >Use F9 para alternar entre os pilotos caso tenha escolhido varios</LI>
			   <LI >Use F12 para marcar ou cancelar ida aos box </LI>
			   <LI >Use a rolagem do mouse para controlar o zoom </LI>
			   <LI >Use as setas do teclado para escolher o tra�ado </LI>			   
			</UL>
			<p style="color: #0084B4;">
				No jogo online:	
			</p>
			<UL>
			   <LI >Jogadores podem criar e editar equipes e pilotos</LI>
			   <LI >Um jogo pode ter ate 24 jogadores simultaneos</LI>
			   <LI >O jogador pode evoluir seu piloto e equipe com pontos ganhos </LI>
			   <LI >Pode-se ver ranking de pilotos, equipes e jogadores</LI>			   
			   <LI >Pode-se criar campeonatos </LI>
			   <LI >26 Circuitos disponiveis para jogar </LI>
			</UL>				
		</div>		
		

		<div id="shots" class="highslide-gallery">
			<a href="fm1.jpg" style="padding-left: 10px; padding-right: 10px;" onclick="return hs.expand(this)"> <img src="fm1.jpg" width="130" height="120" /></a>
			<a href="fm2.jpg" style="padding-left: 10px; padding-right: 10px;" onclick="return hs.expand(this)"> <img src="fm2.jpg" width="130" height="120" /></a>
			<a href="fm3.jpg" style="padding-left: 10px; padding-right: 10px;" onclick="return hs.expand(this)"> <img src="fm3.jpg" width="130" height="120" /></a><br>
			<a href="fm4.jpg" style="padding-left: 10px; padding-right: 10px;" onclick="return hs.expand(this)"> <img src="fm4.jpg" width="130" height="120" /></a>
			<a href="fm5.jpg" style="padding-left: 10px; padding-right: 10px;" onclick="return hs.expand(this)"> <img src="fm5.jpg" width="130" height="120" /></a>
			<a href="fm6.jpg" style="padding-left: 10px; padding-right: 10px;" onclick="return hs.expand(this)"> <img src="fm6.jpg" width="130" height="120" /></a><br>
		</div>
		<br>
		<div style="text-align: center;">
			<div style="text-align: center;	">
				<a id="link"  href="f1maneonlineLite.jnlp" style="text-align: left;">
					Jogar Online
					<img src="webstart.png" border="0">
				</a>	
				&nbsp;&nbsp;&nbsp;&nbsp;
				<a id="link" href="f1maneLite.jnlp" style="text-align: left;">
					Jogar Offline
					<img src="webstart.png" border="0">
				</a>				
			<div>						
		</div>
		<br>
		<div id="shotsPromo" class="highslide-gallery">
			Veja Tambem 
			<a id="link" 
				href="http://www.f1mane.com/mesa11" style="text-align: center;"
				target="_BLANK">  Mesa-11 </a><br><br>
			<a href="http://www.f1mane.com/mesa11/m11-1.jpg" style="padding-left: 10px; padding-right: 10px;" onclick="return hs.expand(this)"> <img src="http://www.f1mane.com/mesa11/m11-1.jpg" width="130" height="120" /></a>
			<a href="http://www.f1mane.com/mesa11/m11-2.jpg" style="padding-left: 10px; padding-right: 10px;" onclick="return hs.expand(this)"> <img src="http://www.f1mane.com/mesa11/m11-2.jpg" width="130" height="120" /></a>
			<a href="http://www.f1mane.com/mesa11/m11-4.jpg" style="padding-left: 10px; padding-right: 10px;" onclick="return hs.expand(this)"> <img src="http://www.f1mane.com/mesa11/m11-4.jpg" width="130" height="120" /></a>
		</div>	
		
	</div>
	</td>
	</tr>
</table>
</body>
</html>