// On click of save button 

$("#save").click(function(){;
// Get the values of the form from the html selected			
var phlebotomistId = $("#selectPhleb option:selected").val(); // phl ID comes from here
var pscId = $("#selectPSC option:selected").val(); // psc Id
var date = $("#date").val();// value comes from form.
var time = $("#time").val(); // time of appointment
var patientID = $("#selectName option:selected").val();  // patientID
var physicianId = $("#date").val(); // physician ID



// Creating an xml of the selected values to send to the server
var xml = '<?xml version="1.0" encoding="utf-8" standalone="no"?>' + 
			'<appointment>' +
			    '<date>' +date+ '</date>' +
			    '<time>' +time+'</time>' +
			    '<patientId>' +patientID+ '</patientId>' +
			    '<physicianId>' +physicianId+ '</physicianId>' +
			    '<pscId>' +pscId+ '</pscId>' + 
			    '<phlebotomistId>' +phlebotomistId+'</phlebotomistId>' + 
			    '<labTests>';
			    // CREATE A ARRAY OF MULTI SELECT OPTIONS
			var selected=[];
			var i=0;
			 $('#selectLabs :selected').each(function(){
				     selected[i++]=$(this).val();
				    });
		// SPLIT THE DX CODE AND LAB TEST CODE
		var res=[];
		for (var j=0;j<selected.length;j++){
			 res[j] = selected[j].split(",");	
		};

		for (var j=0;j<res.length;j++){			
			xml += '<test id='+res[j][0]+  ' dxcode=' +res[j][1]+ '/>' ;
		};
		// CLOSING THE XML ROOT NODE
		xml += '</labTests>' +
			'</appointment>';
			
		//console.log(xml);
		
	});