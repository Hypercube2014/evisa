import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from 'src/app/common/commonComponent';

@Component({
  selector: 'app-user-view',
  templateUrl: './user-view.component.html',
  styleUrls: ['./user-view.component.css']
})
export class UserViewComponent extends BaseComponent implements OnInit {
  
  userDetails: any = []

  constructor(inj: Injector) {
    super(inj);
  }

  ngOnInit(): void {
  }

}
