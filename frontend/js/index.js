var socket;
var socket1;
var tds;
var user_id = Math.floor(Math.random() * 10) + 100;
var index;
var myticketid;
var modalMo = false;
$(document).on("click", "tr", function() {
  var id = $(this).attr('id');
  var pieces = id.split("-");
  // console.log(pieces[1]);
  // var editable = false;
  console.log($(this).children());
  tds = $(this).children();
  myticketid = pieces[1];
  socket1.emit('proposal',
    '{ticket_id:' + pieces[1] + ', user_id:' + user_id + ', cmd:process}');

  $('#exampleModalLong').modal('toggle');
  modalMo = false;

});

$("#modified").click(function() {
  // TODO add emit to modify tickets;
  modalMo = true;
  console.log($('#textarea1').val());
  socket1.emit('proposal',
    '{ticket_id:' + myticketid + ', user_id:' + user_id + ', cmd:close, reply:"' + $('#textarea1').val() + '"}');
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
    console.log('accept');
  });
  socket1.on('refuse', function(data) {
    // socket.emit('proposal', data);
    // TODO modified by others
    $('#exampleModalLong').modal('toggle');
  });
  socket1.on('order', function(data) {
    console.log('this is order.');
    console.log(data);
    // TODO check id in the response, then enable modifying
    // var editable = false;
    var obj = JSON.parse(data);
    if ($('#exampleModalLong').is(':visible')) {
      if (myticketid in obj) {
        console.log("check:" + (obj[myticketid]['userID'] == user_id + ""));
        modify(obj[myticketid]['userID'] == (user_id + "") && obj[myticketid]['cmd'] == "process");
      }
    } else {
      console.log('now in open view');
      var w;
      // TODO
      for (w = 0; w < 10; w += 1) {
        if (obj[index + w + ""]) {
          var cur = obj[index + w + ""];
          var mystr = "ticket-" + (index + w);
          // var uid = $('#ticket-' + (index + w) + ":nth-child(3)");
          // var cmd = $('#ticket-' + (index + w) + ":nth-child(5)");
          // var rpl = $('#ticket-' + (index + w) + ":nth-child(6)");
          // var ticket = $('#ticket-' + (index + w));
          var childrens = document.getElementById(mystr).childNodes;
          // var p;
          // for (p = 0; p < childrens.length; p += 1) {
          //   console.log(childrens[p]);
          // }
          var uid = childrens[3];
          var cmd = childrens[5];
          var rpl = childrens[6];

          // console.log($(mystr).html());
          // console.log(uid.html());
          // console.log(cmd.html());
          // console.log(rpl.html());
          uid.innerHTML = "<td>" + cur["userID"] + "</td>";
          console.log(uid);
          if (cur["cmd"] == 'process') {
            cmd.innerHTML = '<td><span class="badge badge-success">editing</span></td>';
          } else if (cur["cmd"] == 'close') {
            cmd.innerHTML = '<td><span class="badge badge-secondary">closed</span></td>';
          } else {
            cmd.innerHTML = '<td><span class="badge badge-primary">open</span></td>';
          }

          rpl.innerHTML = '<td style = "display:none" >' + cur["reply"]["value"] + "</td>";
        }
      }
    }
  });

  socket1.on('return', function(data) {
    console.log('initialTickets');
    console.log(data);
    initialTickets(data);
  });

  socket.connect();
  socket1.connect();
  // index = Math.floor(Math.random() * 900) + 1;
  index = 0;
  socket1.emit('hope', index);
});

function clearTicketsList() {
  $('#ticketsrows').html('');
}

function initialTickets(data) {
  var i;
  clearTicketsList();
  for (i = 0; i < 10; i += 1) {
    console.log(data[index + i]);
    var current = data[index + i];
    var text = '<tr id="ticket-' + (index + i) + '">';
    text += '<td>' + (index + i) + '</td>';
    var dateAndTime = current['data_time'].split(" ");
    text += '<td>' + dateAndTime[0].substring(1) + '</td>';
    text += '<td>' + dateAndTime[1].substring(0, dateAndTime[1].length - 1) + '</td>';
    text += '<td>' + current['user_id'] + '</td>';
    text += '<td>' + current['description'] + '</td>';

    if (current["status"] == 'process') {
      text += '<td><span class="badge badge-success">editing</span></td>'
    } else if (current["status"] == 'close') {
      // continue;
      text += '<td><span class="badge badge-secondary">closed</span></td>'
    } else {
      // continue;
      text += '<td><span class="badge badge-primary">open</span></td>'
    }
    text += '<td style = "display:none" >' + current['replay'] + '</td></tr>'
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


}

$('.closemodal').click(function() {
  socket1.emit('proposal',
    '{ticket_id:' + myticketid + ', user_id:' + user_id + ', cmd:open}');
  // $('#exampleModalLong').modal('toggle');
});


$('#exampleModalLong').on('hidden.bs.modal', function() {
  if (!modalMo)
    socket1.emit('proposal',
      '{ticket_id:' + myticketid + ', user_id:' + user_id + ', cmd:open}');
  // do something here...
});