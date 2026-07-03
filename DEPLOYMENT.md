# Deployment, SEO, and Traffic Plan

## Production Hosting

Use this split deployment:

- Frontend: Vercel, Netlify, Cloudflare Pages, or Render static Docker service.
- Backend: Render, Railway, Fly.io, AWS Elastic Beanstalk, or a small VPS.
- Database: Managed MySQL such as PlanetScale, Railway MySQL, Aiven, AWS RDS, or DigitalOcean Managed MySQL.

## Required Environment Variables

Backend:

```text
MYSQL_USER=
MYSQL_PASSWORD=
SMTP_HOST=
SMTP_PORT=587
SMTP_USERNAME=
SMTP_PASSWORD=
MAIL_ENABLED=true
OTP_EXPIRY_MINUTES=10
TOKEN_EXPIRY_DAYS=30
APP_CORS_ALLOWED_ORIGIN=https://your-domain.com
GOOGLE_CLIENT_ID=your_google_client_id
DB_POOL_MAX_SIZE=10
DB_POOL_MIN_IDLE=2
```

Frontend:

```text
VITE_API_BASE_URL=https://your-backend-domain.com/api
VITE_GOOGLE_CLIENT_ID=your_google_client_id
```

## SEO Checklist

- Replace `smart-revision-scheduler.example.com` in `frontend/index.html`, `robots.txt`, and `sitemap.xml` with your real domain.
- Add a real `frontend/public/og-image.png` preview image.
- Register the domain in Google Search Console.
- Submit `/sitemap.xml`.
- Add public content pages later: `/features`, `/spaced-repetition`, `/study-tips`, and `/exam-revision-planner`.
- For best SEO, migrate marketing pages to Next.js or Astro later because static/SSR pages rank more reliably than a pure private React dashboard.

## Traffic Capacity

The current architecture can handle a good student-project launch if deployed correctly:

- Serve frontend through CDN/static hosting.
- Use managed MySQL with indexes on revision dates, users, tokens, and OTPs.
- Scale backend horizontally behind the hosting provider load balancer.
- Keep browser assets cached for one year through Nginx.
- Use `/actuator/health` as the backend health-check URL.
- Add Redis later for OTP storage, rate limiting, and session/token checks.
- Add request rate limiting before public launch, especially on `/api/auth/register/request-otp`, `/api/auth/login`, and `/api/auth/password/request-reset`.
- Add monitoring: uptime checks, backend logs, database CPU/connection alerts.

## Important Reality About "Top In Search"

Good SEO score helps, but no one can guarantee top ranking immediately. Ranking depends on domain age, page speed, backlinks, useful content, search competition, and Google indexing. The app now has technical SEO basics; the next ranking step is adding public educational content around revision planning and spaced repetition.
