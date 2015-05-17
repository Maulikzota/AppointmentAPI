// ON CLICK OF THE MODAL WE CREATE A TABLE , 
//TO FIND THE VALUE WHICH TABLE ROW WAS SELECTED BY USER

$("#appointmentListAll").on("click","tr",function(){
	//ON CLICK FINDING THE SELECTED VALUE
   $(this).addClass('selected').siblings().removeClass('selected');    
   var value=$(this).find('td:first').html();
   //alert(value);  
   //console.log($(this));
});

$("#appointmentListAll").on("click",".editbutton",function(){
  //go to edit page
});