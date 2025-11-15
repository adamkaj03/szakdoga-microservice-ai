import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {HttpClientModule} from "@angular/common/http";

import { AppComponent } from './app.component';
import {BookService} from "./services/book.service";
import {RouterModule, Routes} from "@angular/router";
import { AuthComponent } from './auth/auth.component';
import {FormsModule} from "@angular/forms";
import { LoadingSpinnerComponent } from './shared/loading-spinner/loading-spinner.component';
import { LogoutComponent } from './logout/logout.component';
import { BookPageComponent } from './book-page/book-page.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { OrderPageComponent } from './order-page/order-page.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import { ToastrModule } from 'ngx-toastr';
import { CartPageComponent } from './cart-page/cart-page.component';
import { EmptyCartComponent } from './empty-cart/empty-cart.component';
import { ShippingDetailsComponent } from './shipping-details/shipping-details.component';
import {AuthGuard} from "./services/authGurard";
import {RoleGuard} from "./services/roleGuard";
import { ForbiddenPageComponent } from './forbidden-page/forbidden-page.component';
import { NewItemPageComponent } from './new-item-page/new-item-page.component';
import { ArchivePageComponent } from './archive-page/archive-page.component';
import { StatusTranslatePipe } from './pipes/status-translate.pipe';
import { NewArchiveComponent } from './new-archive/new-archive.component';
import { FooterComponent } from './footer/footer.component';
import { LandingPageComponent } from './landing-page/landing-page.component';
import { FeaturedCarouselComponent } from './featured-carousel/featured-carousel.component';
import { RecommendedBooksComponent } from './recommended-books/recommended-books.component';
import { AllBooksComponent } from './all-books/all-books.component';
import { SearchResultComponent } from './search-result/search-result.component';
import { ChatButtonComponent } from './chat-button/chat-button.component';
import { ChatWindowComponent } from './chat-window/chat-window.component';
import { ChatService } from './services/chat.service';
import { SearchBarComponent } from './search-bar/search-bar.component';

const appRoutes: Routes = [
  {path: "", component: LandingPageComponent},
  {path: "search", component: SearchResultComponent},
  {path: "signin", component: AuthComponent},
  {path: "book/:title", component: BookPageComponent},
  {path: "orders", component: OrderPageComponent, canActivate: [RoleGuard], data: {
      expectedRole: 'ADMIN'}
  },
  {path: "newItem", component: NewItemPageComponent, canActivate: [RoleGuard], data: {
      expectedRole: 'ADMIN'}
  },
  {path: "archives", component: ArchivePageComponent, canActivate: [RoleGuard], data: {
      expectedRole: 'ADMIN'}
  },
  {path: "new-archive", component: NewArchiveComponent, canActivate: [RoleGuard], data: {
    expectedRole: 'ADMIN'}
  },
  {path: "cart", component: CartPageComponent},
  {path: "forbidden", component: ForbiddenPageComponent},
  {path: "shipping", component: ShippingDetailsComponent, canActivate: [AuthGuard]}
]

@NgModule({
  declarations: [
    AppComponent,
    AuthComponent,
    LoadingSpinnerComponent,
    LogoutComponent,
    BookPageComponent,
    NavBarComponent,
    OrderPageComponent,
    CartPageComponent,
    EmptyCartComponent,
    ShippingDetailsComponent,
    ForbiddenPageComponent,
    NewItemPageComponent,
    ArchivePageComponent,
    StatusTranslatePipe,
    NewArchiveComponent,
    FooterComponent,
    LandingPageComponent,
    FeaturedCarouselComponent,
    RecommendedBooksComponent,
    AllBooksComponent,
    SearchResultComponent,
    ChatButtonComponent,
    ChatWindowComponent,
    SearchBarComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes),
    FormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      timeOut: 3000,
      positionClass: 'toast-bottom-center',
    })
  ],
  providers: [BookService,
              RoleGuard,
              ChatService],
  bootstrap: [AppComponent]
})
export class AppModule { }
