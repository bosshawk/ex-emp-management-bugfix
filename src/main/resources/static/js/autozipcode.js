/**
 *
 */

$(function(){
  $("#search").click( function(){
	  console.log($('#zipCode').val());
      $.ajax({
        url: "http://zipcoda.net/api",
        dataType: "jsonp",
        data: {
          zipcode: $('#zipCode').val()
        },
      })
      .done(function(data) {
        console.log(data);
        $("#address").val(data.items[0].address);
      })
      .fail(function() {
        window.alert('正しい結果を得られませんでした。')
      });
  });
});
