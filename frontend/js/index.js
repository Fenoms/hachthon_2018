var socket;
var socket1;
var tds;
var user_id = Math.floor(Math.random() * 10) + 100;
$(document).on("click", "tr", function() {
  var id = $(this).attr('id');
  var pieces = id.split("-");
  console.log(pieces[1]);
  // var editable = false;
  console.log($(this).children());
  tds = $(this).children();
  var proposal = ('proposal',
    '{ticket_id:' + pieces[1] + ', user_id:' + user_id + ', cmd:process}');
  console.log(proposal);
  socket1.emit(proposal);
});

$("#modified").click(function() {
  console.log("Handler for .click() called.");
  // TODO add emit to modify tickets;
  $('#exampleModalLong').modal('toggle');
});


$(document).ready(function() {
  socket = io('http://192.168.137.19:12345');
  socket.on('connect', function() {});
  socket.on('event', function(data) {
    console.log(data);
  });
  socket.on('disconnect', function() {});



  socket1 = io('http://192.168.137.30:12346');
  socket1.on('connect', function() {});
  socket1.on('event', function(data) {
    console.log(data);
  });
  socket1.on('disconnect', function() {});
  socket1.on('accept', function(data) {
    socket.emit('proposal', data);
  });
  socket1.on('refuse', function(data) {
    socket.emit('proposal', data);
  });
  socket1.on('order', function(data) {
    console.log('this is order.')
    console.log(data);
    // TODO check id in the response, then enable modifying
    var editable = false;
    // for ()
    // modify(user_id == );
  });

  socket1.on('return', function(data) {
    console.log('initialTickets');
    console.log(data);
    initialTickets(data);
  });

  socket.connect();
  socket1.connect();
  var index = Math.floor(Math.random() * 900) + 1;
  socket1.emit('hope', index);
});

function clearTicketsList() {
  $('#ticketsrows').html();
}

function initialTickets(data) {
  var i;
  clearTicketsList();
  for (i = 0; i < data.length; i += 1) {
    var text = '<tr id="ticket-' + data[i][0] + '">';
    var j;
    for (j = 0; j < data[i].length; j += 1) {
      text += '<td>' + data[i][0] + '</td>';
    }
    if (data[i][5] == 'process') {
      text += '<td><span class="badge badge-success">editing</span></td>'
    } else if (data[i][5] == 'closed') {
      text += '<td><span class="badge badge-info">closed</span></td>'
    } else {
      text += '<td><span class="badge badge-primary">open</span></td>'
    }
    $('#ticketsrows').append(text);
  }
}

function modify(editable) {
  var text = "<div><h5>No. ";
  text += $(tds[0]).text() + "\t";
  text += '<span class="text-info">';
  text += $(tds[1]).text() + "\t";
  text += $(tds[2]).text() + "\t";
  text += '</span>';
  text += $(tds[3]).text() + "\t";
  if (editable) {
    text += '<span class = "badge badge-success float-right"> Editing.. </span>' + "\t";
  } else {
    text += '<span class = "badge badge-secondary float-right"> Read - only </span>' + "\t";
  }

  text += "</h5><p>" + $(tds[4]).text() + "</p>";
  text += "</div>";
  console.log(text);
  $('#ticket-detail').html(text);
  if (editable) {
    $('#editor').html('<form><div class="form-group">' +
      '<label for="exampleFormControlTextarea1">Solution:</label>' +
      '<textarea class="form-control" id="textarea1" rows="5"></textarea>' +
      '</div></form>');
  } else {
    var solution = '<h5>Solution:</h5><p>' + $(tds[6]).text() + '</p>';
    $('#editor').html(solution);
  }

  $('#exampleModalLong').modal('toggle');

}