import { Component, OnInit ,Injector} from '@angular/core';
import { BaseComponent } from '../common/commonComponent';
@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent extends BaseComponent implements OnInit {

  public moduleType:boolean=false;
  constructor(inj:Injector) {
    super(inj);
    this.translate.get('DEFAULT_TITLE').subscribe((translatedTitle) => {
      this.titleService.setTitle(translatedTitle);
    })
   }

  ngOnInit(): void {
  }

}
