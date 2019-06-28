var electron = require('electron');

electron.app.on('ready', function createWindow () {

  new electron.BrowserWindow(
      {width: 800, height: 600}
    ).loadURL('file://' + __dirname + "/index.html")

    var menuTemplate = electron.Menu.buildFromTemplate( [
      {
        label: '入门' ,
        submenu: [
          {
            label: 'About ...',
            click: () => {
              console.log('HelloWorld on electron');
            }
          }, 
          {
            label: '退出',
            click: () => {
              electron.app.quit();
            } 
          }
        ]
      }
    
    ]);
    electron.Menu.setApplicationMenu(menuTemplate);

} );
