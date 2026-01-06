import { Component, OnInit,Injector } from '@angular/core';
import { BaseComponent } from '../../common/commonComponent';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent extends BaseComponent implements OnInit {

  public base64Image='data:image/png;base64,'
  constructor(inj:Injector) {
    super(inj);
    
   }

  ngOnInit(): void {
  
  }
  

}
