
var SUCCESS = 'success';
var ERROR = 'error';

var serverErrorMessage = 'Oops, something wrong :(';

var deptList = {};

$(document).ready(function() {
    $('#empDataTable').DataTable( {
        "ajax": {
            "url": "/emp/list",
            "dataType": "json"
        },
         "columns": [
                    { "data": "employee.name" },
                    { "data": "employee.email" },
                    { "data": "employee.position" },
                    { "data": "department.name" },
                    { data: "employee.id" ,
                     "render": function ( data) {
                                  return '<i id=" ' + data +' " class="edit-button glyphicon glyphicon-edit cursorPointer" ></i>';
                                }
                            },
                     { data: "employee.id" ,
                        "render": function ( data ) {
                                   return '<i id=" ' + data +' " class="remove-button glyphicon glyphicon-trash cursorPointer"></i>';
                               }
                     }

                ]
    } );

    var tableEmp = $('#empDataTable').DataTable();


    $('#deptDataTable').DataTable( {
        "ajax": {
            "url": "/dept/list",
            "dataType": "json"
        },
         "columns": [
                    { "data": "name" },
                    { data: "id" ,
                     "render": function ( data) {
                                  return '<i id=" ' + data +' " class="dept-edit-button glyphicon glyphicon-edit cursorPointer" ></i>';
                                }
                            },
                     { data: "id" ,
                        "render": function ( data ) {
                                   return '<i id=" ' + data +' " class="dept-remove-button glyphicon glyphicon-trash cursorPointer"></i>';
                               }
                     }

                ]
    } );

    var tableDept = $('#deptDataTable').DataTable();
	loadDeptDropDown();
	
	function loadDeptDropDown() {
		$("#deptSelect").empty();
		$("#editDeptSelect").empty();
		$.ajax({
         url: "/dept/list",
         type: "GET",
         success:function(response) {
         	if(response.status == SUCCESS) {            	
             deptList = response.data;
             console.log(deptList);
             $.each(deptList, function(index) {
				$("#deptSelect").append("<option value='" + deptList[index].id + "'>" + deptList[index].name + "</option>");
				$("#editDeptSelect").append("<option value='" + deptList[index].id + "'>" + deptList[index].name + "</option>");
			});   
             
			} else {
            	showErrorAlert(serverErrorMessage);
            }
		 },
         error: function(){
             showErrorAlert(serverErrorMessage);
         }
      });
	}
    // Delete employee event
    $("body").on( 'click', '.remove-button', function () {
        var currentRow = $(this);
        var employeeId = $(this).attr('id').trim();
         bootbox.confirm("Are you sure?"+employeeId, function(result) {
            if(result) {
                    $.ajax({
                     url: "/emp?empId="+employeeId,
                     type: "DELETE",
                     success:function(response){
                               if(response.status == SUCCESS) {
                                  showSuccessAlert(response.msg);
                                  tableEmp.row(currentRow.parents('tr') ).remove().draw();
                              } else {
                                  showErrorAlert(serverErrorMessage);
                              }
                        },
                     error: function(){
                          showErrorAlert(serverErrorMessage);
                       }
                  });
            } else {
               //
              }
         });
    });
     

     // Edit employee event
     $("body").on( 'click', '.edit-button', function () {
            var employeeId = $(this).attr('id').trim();
             $.ajax({
                   url: "/emp",
                   type: "GET",
                   data: {empId: employeeId},
                   success:function(response){
                             $('#empEditModal').modal('show');
                             $.each(response.data, function(key, value){
                             	if(key=='departmentId') {
                             		$("#editDeptSelect").val(value)
                             	} else {
                                	$('#empEditForm input[name="'+key+'"]').val(value);
                                }
                             });
                             
                      },
                   error: function(){
                             showErrorAlert(serverErrorMessage);
                     }
                });
          });


$('#empModal').on('shown.bs.modal', function () {
  $('#empForm').trigger("reset");
});




	// Show success alert message
	var showSuccessAlert = function (message) {
	   	$.toaster({ priority : 'success', title : 'Success', message : message});
	}
	
	// Show error alert message
	var showErrorAlert = function (message) {
	    $.toaster({ priority : 'danger', title : 'Error', message : message});
	}

	// Convert form data in JSON format
	$.fn.serializeObject = function() {
	           var o = {};
	           var a = this.serializeArray();
	           $.each(a, function() {
	                    if (o[this.name] !== undefined) {
	                        if (!o[this.name].push) {
	                            o[this.name] = [o[this.name]];
	                        }
	                        o[this.name].push(this.value || '');
	                    } else {
	                          if(this.name == 'id' || this.name == 'departmentId') {
	                             o[this.name] = parseInt(this.value) || 0;
	                          } else {
	                         	o[this.name] = this.value || '';
	                         }
	                    }
	               });
	            return JSON.stringify(o);
    };

// Handling form submission for create new employee
      $('#empForm').on('submit', function(e){
         var formData = $("#empForm").serializeObject();
         var empTable = $('#empDataTable').dataTable();
          e.preventDefault();
           $.ajax({
                url: "/emp",
                type: "POST",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                data: formData,
                success:function(response){
                   if(response.status == "success") {
                         $('#empModal').modal('hide');
                         $('#empDataTable').DataTable().ajax.reload();
                         showSuccessAlert(response.msg);
                   } else {
                        $('#empModal').modal('hide');
                        showErrorAlert(response.msg);
                   }
                },
                error: function(){
                    $('#empModal').modal('hide');
                    showErrorAlert(serverErrorMessage);
                }

            });
            return false;
      });

	// Handling form submission for update employee
	$('#empEditForm').on('submit', function(e){
       var formData = $("#empEditForm").serializeObject();
        e.preventDefault();
         $.ajax({
              url: "/emp",
              type: "PUT",
              contentType: "application/json; charset=utf-8",
              dataType: "json",
              data: formData,
              success:function(response){
                 if(response.status == SUCCESS) {
                       $('#empEditModal').modal('hide');
                       $('#empDataTable').DataTable().ajax.reload();
                       showSuccessAlert(response.msg)
                 } else {
                    $('#empEditModal').modal('hide');
                    showErrorAlert(response.msg);
                 }
              },
              error: function(){
                  $('#empEditModal').modal('hide');
                  showErrorAlert(serverErrorMessage);
              }

          });
          return false;
    });
            
//DEPARTMENT
	$("body").on( 'click', '.dept-remove-button', function () {
        var currentRow = $(this);
        var departmentId = $(this).attr('id').trim();
         bootbox.confirm("Are you sure?", function(result) {
            if(result) {
                    $.ajax({
                     url: "/dept?deptId="+departmentId,
                     type: "DELETE",
                     success:function(response){
                               if(response.status == SUCCESS) {
                                  showSuccessAlert(response.msg);
                                  tableDept.row(currentRow.parents('tr') ).remove().draw();
                              } else {
                                  showErrorAlert(serverErrorMessage);
                              }
                        },
                     error: function(xhr, data, thrownError){
                			if(xhr.status==400) {
                				showErrorAlert(xhr.responseJSON.msg)
                			} else {
	                          	showErrorAlert(serverErrorMessage);
	                          }	
                       }
                  });
            } else {
               //
              }
         });
    });
     
	$("body").on( 'click', '.dept-edit-button', function () {
	    var departmentId = $(this).attr('id').trim();
	     $.ajax({
	           url: "/dept",
	           type: "GET",
	           data: {deptId: departmentId},
	           success:function(response){
	                     $('#deptEditModal').modal('show');
	                     $.each(response.data, function(key, value){
	                        $('#deptEditForm input[name="'+key+'"]').val(value);
	                     });
	              },
	           error: function(){
	                     showErrorAlert(serverErrorMessage);
	             }
	     });
    });


	$('#deptModal').on('shown.bs.modal', function () {
	  $('#deptForm').trigger("reset");
	});


	$('#deptForm').on('submit', function(e){
         var formData = $("#deptForm").serializeObject();
         var deptTable = $('#deptDataTable').dataTable();
          e.preventDefault();
           $.ajax({
                url: "/dept",
                type: "POST",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                data: formData,
                success:function(response){
                   if(response.status == "success") {
                         $('#deptModal').modal('hide');
                         var newDept = jQuery.parseJSON(formData);
                         newDept['id'] = response.data['id'];
                         deptTable.fnAddData([newDept]);
                         showSuccessAlert(response.msg);
                   } else {
                        $('#deptModal').modal('hide');
                        showErrorAlert(response.msg);
                   }
                },
                error: function(){
                    $('#deptModal').modal('hide');
                    showErrorAlert(serverErrorMessage);
                }

            });
            return false;
	});


	$('#deptEditForm').on('submit', function(e) {
		var formData = $("#deptEditForm").serializeObject();
	    e.preventDefault();
	     $.ajax({
	          url: "/dept",
	          type: "PUT",
	          contentType: "application/json; charset=utf-8",
	          dataType: "json",
	          data: formData,
	          success:function(response){
	             if(response.status == SUCCESS) {
	                   $('#deptEditModal').modal('hide');
	                   $('#empDataTable').DataTable().ajax.reload();
	                   $('#deptDataTable').DataTable().ajax.reload();
	                   loadDeptDropDown();
	                   showSuccessAlert(response.msg)
	             } else {
	                $('#empEditModal').modal('hide');
	                showErrorAlert(response.msg);
	             }
	          },
	          error: function(){
	              $('#deptEditModal').modal('hide');
	              showErrorAlert(serverErrorMessage);
	          }
	
	      });
	      return false;
	});

	

});


