// ON ALL APOINTMENT CLICK SHOW THE MODAL 
$("#showAllAppointment").click(function(){;
// CREAATING THE TABLE TO PUT ON THE MODAL
var htmlTableData = '<table id="table123" class="table-hover" style="width:100%"><tr> <th>Appt ID</th> <th>Name</th> <th>Phlebotomist</th> <th>PSC</th> <th>Date</th> <th>Time</th> </tr>';
// CREATING THE TABLE 
$(xml).find("appointment").each(function(){
// FETCHING THE VALUES FROM THE XML PARSING
	htmlTableData+='<tr><td>' + $(this).attr("id") + '</td>';
	htmlTableData+='<td>' + $(this).find("patient").find("name").text() + '</td>';
	htmlTableData+='<td>' + $(this).find("phlebotomist").text() + '</td>';
	htmlTableData+='<td>' + $(this).find("psc").text() + '</td>';
	htmlTableData+='<td>' + $(this).attr("date") + '</td>';
	htmlTableData+='<td>' + $(this).attr("time") + '</td>';
  htmlTableData+='<td>' + "<button data-id="+$(this).attr("id")+" class='btn btn-link editbutton'>edit</button>" + '</td></tr>';

});
	htmlTableData+='</table>';
	// console.log(htmlTableData);
  // SENDING THE VALUES TO THE DIV OF THE HTML PAGE
	$("#appointmentListAll").html(htmlTableData);
  	});